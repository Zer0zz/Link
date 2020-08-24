package com.leconssoft.common.widgt.swiprefresh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

import com.leconssoft.common.baseUtils.Utils;
import com.leconssoft.common.widgt.swiprefresh.intf.SwipProgressBarRunningListener;
import com.leconssoft.common.widgt.swiprefresh.intf.SwipeRefreshScrollInterface;


/**
 * The SwipeRefreshLayout should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The SwipeRefreshLayout
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and progress
 * animation, call setEnabled(false) on the view.
 *
 * <p> This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The SwipeRefreshLayout does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.</p>
 */
public class SwipeRefreshLayout extends ViewGroup implements SwipeRefreshScrollInterface {
    private static final String LOG_TAG = SwipeRefreshLayout.class.getSimpleName();

    private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300;
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final float PROGRESS_BAR_HEIGHT = 60;
    private static final float PROGRESS_BAR_HEIGHT_BOTTOM = 30;
    private static final float MAX_SWIPE_DISTANCE_FACTOR = .2f;
    private static final int REFRESH_TRIGGER_DISTANCE = 90;
    private static final int INVALID_POINTER = -1;
    private static final float TOUCH_MOVE_FLAG = .45f;

    private BaseSwipProgressBar mProgressBar; //the thing that shows progress is going
    private BaseSwipProgressBar mProgressBarBottom;
    private View mTarget; //the content that gets pulled down
    private int mOriginalOffsetTop;
    private OnRefreshListener mRefreshListener;
    private OnLoadListener mLoadListener;
    private int mFrom;
    private boolean mRefreshing = false;
    private boolean mLoading = false;
    private int mTouchSlop;
    private float mDistanceToTriggerSync = -1;//下拉/上拉的阈值，当超过这个距离时，松开手指开始加载数据
    private int mMediumAnimationDuration;
    private float mFromPercentage = 0;
    private float mCurrPercentage = 0;
    private int mProgressBarHeight;
    private int mProgressBarHeightBottom;
    private int mCurrentTargetOffsetTop;

    private float mInitialMotionY;
    private float mLastMotionY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private final AccelerateInterpolator mAccelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[] {
            android.R.attr.enabled
    };
    private Mode mMode = Mode.getDefault();
    //之前手势的方向，为了解决同一个触点前后移动方向不同导致后一个方向会刷新的问题，
    //这里Mode.DISABLED无意义，只是一个初始值，和上拉/下拉方向进行区分
    private Mode mLastDirection = Mode.DISABLED;
    //当子控件移动到尽头时才开始计算初始点的位置
    private float mStartPoint;
    private float mStartLocaY;
    private boolean up;
    private boolean down;
    //数据不足一屏时是否打开上拉加载模式
    private boolean loadNoFull = false;

    //当手指松开时是否能下拉刷新
    private boolean canUpLoading = false;
    //当手指松开时是否能上拉刷新
    private boolean canDownLoading = false;

    //是否能下拉刷新
    private boolean canRefresh = true;
    //是否能上拉加载
    private boolean canLoadMore = true;
    private boolean isLoadMoreStop = false;

    private boolean refreshingToStop = false;
    private boolean loadingToStop = false;

    private boolean isOutRefreshLine = false;
    private boolean runningMove = false;
    private float mToucheventTargetOffsetTop;
    private float mStartPointX;

    private SwipeRefreshScrollInterface scrollAble;

    //对下拉或上拉进行复位
    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop = 0;
            int mTempOriginalOffsetTop = mOriginalOffsetTop;
            if(isOutRefreshLine){//是否超过加载阈值
                if(mProgressBar.isRunning()){
                    mTempOriginalOffsetTop += mProgressBarHeight;
                }
                if(mProgressBarBottom.isRunning())
                    mTempOriginalOffsetTop -= mProgressBarHeightBottom;
            }
            if (mFrom != mTempOriginalOffsetTop) {
                targetTop = (mFrom + (int)((mTempOriginalOffsetTop - mFrom) * interpolatedTime));
            }
            int offset = targetTop - mTarget.getTop();
            if(Math.abs(targetTop) < Math.abs(mTempOriginalOffsetTop * .2) || targetTop == 0)
                mReturningToStart = false;
            setTriggerPercentage(targetTop, mDistanceToTriggerSync);
            //注释掉这里，不然上拉后回复原位置会很快，不平滑
//            final int currentTop = mTarget.getTop();
//            if (offset + currentTop < 0) {
//                offset = 0 - currentTop;
//            }
            setTargetOffsetTopAndBottom(offset);
        }
    };

    //监听，回复初始位置
    private final Animation.AnimationListener mReturnToStartPositionListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // Once the target content has returned to its start position, reset
            // the target offset to 0
            mCurrentTargetOffsetTop = 0;
            if(mProgressBar.isRunning())
                mCurrentTargetOffsetTop = mProgressBarHeight;
            if(mProgressBarBottom.isRunning())
                mCurrentTargetOffsetTop = mProgressBarHeightBottom;
            mLastDirection = Mode.DISABLED;
            mReturningToStart = false;
            refreshingToStop = false;
            loadingToStop = false;
        }
    };

    //回复进度条百分比
    private final Animation.AnimationListener mShrinkAnimationListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            mCurrPercentage = 0;
        }
    };

    //回复初始位置
    private final Runnable mReturnToStartPosition = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(),
                    mReturnToStartPositionListener);
        }

    };

    // Cancel the refresh gesture and animate everything back to its original state.
    //取消刷新手势并将子控件状态重置
    private final Runnable mCancel = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            isOutRefreshLine = mCurrentTargetOffsetTop >= mProgressBarHeight || mCurrentTargetOffsetTop <= -mProgressBarHeight;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(),
                    mReturnToStartPositionListener);
        }

    };

    private SwipProgressBarRunningListener runningListener = new SwipProgressBarRunningListener() {
        @Override
        public void onRealFinish() {
            isOutRefreshLine = false;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(),
                    mReturnToStartPositionListener);
            if(isLoadMoreStop){
                mTarget.scrollBy(0, mProgressBarHeightBottom);
            }
        }

        @Override
        public void onStart() {
        }
    };

    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     * @param context
     */
    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     * @param context
     * @param attrs
     */
    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);

        setWillNotDraw(false);
        mProgressBar = new LogoProgressBar(this, Utils.dip2px(context, 24), Utils.sp2px(context, 3));
        mProgressBar.setRunningListener(runningListener);
        mProgressBarBottom = new LoadMoreProgressBar(this, Utils.sp2px(context, 13));
        mProgressBarBottom.setRunningListener(runningListener);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mProgressBarHeight = (int) (metrics.density * PROGRESS_BAR_HEIGHT);
        mProgressBarHeightBottom = (int) (metrics.density * PROGRESS_BAR_HEIGHT_BOTTOM);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mAccelerateInterpolator = new AccelerateInterpolator(ACCELERATE_INTERPOLATION_FACTOR);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();

        scrollAble = this;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(mCancel);
        removeCallbacks(mReturnToStartPosition);
        mReturningToStart = false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mReturnToStartPosition);
        removeCallbacks(mCancel);
        mReturningToStart = false;
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    //对子控件进行移动
    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
        mAnimateToStartPosition.setAnimationListener(listener);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mTarget.startAnimation(mAnimateToStartPosition);
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mLoadListener = listener;
    }

    //设置进度条的显示百分比
    private void setTriggerPercentage(float currentDiff, float total) {
        float percent = mAccelerateInterpolator.getInterpolation(
                Math.min(total, Math.abs(currentDiff)) / total);
        if (percent == 0f) {
            // No-op. A null trigger means it's uninitialized, and setting it to zero-percent
            // means we're trying to reset state, so there's nothing to reset in this case.
            mCurrPercentage = 0;
            return;
        }
        mCurrPercentage = percent;
        if (((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH))
                && mLastDirection != Mode.PULL_FROM_END && !mLoading && !refreshingToStop)
        {
            mProgressBar.setTriggerPercentage(percent);
        }
        else if(((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH))
                && mLastDirection != Mode.PULL_FROM_START && !mRefreshing && !loadingToStop)
        {
            percent = mAccelerateInterpolator.getInterpolation(
                    Math.min(total * .5f, Math.abs(currentDiff)) / (total * .5f));
            mProgressBarBottom.setTriggerPercentage(percent);
        }
        mCurrPercentage = percent;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            ensureTarget();
            mCurrPercentage = 0;
            mRefreshing = refreshing;
            if (mRefreshing) {
                isOutRefreshLine = mCurrentTargetOffsetTop >= mProgressBarHeight || mCurrentTargetOffsetTop <= -mProgressBarHeightBottom;
                if(mCurrentTargetOffsetTop == getPaddingTop() && !mProgressBar.isRunning()){
                    isOutRefreshLine = true;
                }
                mProgressBar.start();
                mReturnToStartPosition.run();
            } else {
                mLastDirection = Mode.DISABLED;
                mProgressBar.stop();
            }
        }
    }

    public void setLoading(boolean loading) {
        if (mLoading != loading) {
            ensureTarget();
            mCurrPercentage = 0;
            mLoading = loading;
            if (mLoading) {
                isOutRefreshLine = mCurrentTargetOffsetTop >= mProgressBarHeight || mCurrentTargetOffsetTop <= -mProgressBarHeightBottom;
                mProgressBarBottom.start();
                mReturnToStartPosition.run();
            } else {
                mLastDirection = Mode.DISABLED;
                mProgressBarBottom.stop();
            }
        }
    }

    /**
     * @deprecated Use {@link #setColorSchemeResources(int, int, int, int)}
     */
    @Deprecated
    private void setColorScheme(int colorRes1, int colorRes2, int colorRes3, int colorRes4) {
        setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
    }

    /**
     * Set the four colors used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     */
    public void setTopColor(int colorRes1, int colorRes2, int colorRes3,
                            int colorRes4)
    {
        setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
    }

    public void setBottomColor(int colorRes1, int colorRes2, int colorRes3,
                               int colorRes4)
    {
        setColorSchemeResourcesBottom(colorRes1, colorRes2, colorRes3, colorRes4);
    }

    public void setColor(int colorRes1, int colorRes2, int colorRes3,
                         int colorRes4){
        setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
        setColorSchemeResourcesBottom(colorRes1, colorRes2, colorRes3, colorRes4);
    }

    private void setColorSchemeResources(int colorRes1, int colorRes2, int colorRes3,
                                         int colorRes4) {
        final Resources res = getResources();
        setColorSchemeColors(res.getColor(colorRes1), res.getColor(colorRes2),
                res.getColor(colorRes3), res.getColor(colorRes4));
    }

    private void setColorSchemeResourcesBottom(int colorRes1, int colorRes2, int colorRes3,
                                               int colorRes4) {
        final Resources res = getResources();
        setColorSchemeColorsBottom(res.getColor(colorRes1), res.getColor(colorRes2),
                res.getColor(colorRes3), res.getColor(colorRes4));
    }

    /**
     * Set the four colors used in the progress animation. The first color will
     * also be the color of the bar that grows in response to a user swipe
     * gesture.
     */
    private void setColorSchemeColors(int color1, int color2, int color3, int color4) {
        ensureTarget();
        mProgressBar.setColorScheme(color1, color2, color3, color4);
    }

    private void setColorSchemeColorsBottom(int color1, int color2, int color3, int color4) {
        ensureTarget();
        mProgressBarBottom.setColorScheme(color1, color2, color3, color4);
    }
    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     *         progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    public boolean isLoading() {
        return mLoading;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid out yet.
        if (mTarget == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException(
                        "SwipeRefreshLayout can host only one direct child");
            }
            mTarget = getChildAt(0);
            if(mTarget instanceof RecyclerView && canLoadMore){
                ((RecyclerView)mTarget).addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //当前屏幕显示的区域高度
                        int screenHeight = ((RecyclerView)mTarget).computeVerticalScrollExtent();
                        //整个RecycleView控件的高度
                        int totalHeight = ((RecyclerView)mTarget).computeVerticalScrollRange();
                        //当前屏幕之前滑过的距离
                        int scrollOffSet = ((RecyclerView)mTarget).computeVerticalScrollOffset();
                        //当recycleView滑动到底部时 screenHeight + scrollOffSet = totalHeight
                        if(totalHeight - scrollOffSet <= (float)screenHeight*1.5f && !mProgressBarBottom.isRunning() && canLoadMore)
                            startLoad();
                    }
                });
            }
            mOriginalOffsetTop = mTarget.getTop() + getPaddingTop();
        }
        //设置下拉/上拉阈值
        if (mDistanceToTriggerSync == -1) {
            if (getParent() != null && ((View)getParent()).getHeight() > 0) {
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToTriggerSync = (int) Math.min(
                        ((View) getParent()) .getHeight() * MAX_SWIPE_DISTANCE_FACTOR,
                        REFRESH_TRIGGER_DISTANCE * metrics.density);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canRefresh)
            mProgressBar.draw(canvas);
        if(canLoadMore)
            mProgressBarBottom.draw(canvas);
        super.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width =  getMeasuredWidth();
        final int height = getMeasuredHeight();
        mProgressBar.setBounds(0, 0, width, mProgressBarHeight);
        if (getChildCount() == 0) {
            return;
        }
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = mCurrentTargetOffsetTop + getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        mProgressBarBottom.setBounds(0, height-mProgressBarHeightBottom, width, height);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (getChildCount() > 0) {
            getChildAt(0).measure(
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                            MeasureSpec.EXACTLY));
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.app
     */
    public boolean canChildScrollUp(float x, float y) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    public boolean canChildScrollDown(float x, float y) {
        if (android.os.Build.VERSION.SDK_INT < 14) {//SDK14以下暂不做支持
//            if (mTarget instanceof AbsListView) {
//                final AbsListView absListView = (AbsListView) mTarget;
//                return absListView.getChildCount() > 0
//                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
//                                .getTop() < absListView.getPaddingTop());
//            } else {
//                return mTarget.getScrollY() > 0;
//            }
            return false;
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);

//        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
//            mReturningToStart = false;
//        }

        if (!isEnabled() && mReturningToStart) {
            // Fail fast if we're not in a state where a swipe is possible
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mInitialMotionY = ev.getY();
                mStartPointX = ev.getX();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mStartPoint = mInitialMotionY;
                mStartLocaY = ev.getRawY();
                runningMove = false;
                mToucheventTargetOffsetTop = mCurrentTargetOffsetTop;

                up = scrollAble.canChildScrollUp(ev.getRawX(), ev.getRawY());
                down = scrollAble.canChildScrollDown(ev.getRawX(), ev.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
//                final float yDiff = y - mInitialMotionY;
                final float yDiff = (y - mStartPoint) * TOUCH_MOVE_FLAG;
                final float xDiff = (x - mStartPointX) * TOUCH_MOVE_FLAG;

                //手指垂直滑动角度小于30°时不判断为垂直滑动
                if(Math.abs(yDiff)/Math.abs(xDiff) <= Math.tan(Math.toRadians(30))){
                    mIsBeingDragged = false;
                    return false;
                }

                Log.d("NestedScroll", "onInterceptTouchEvent ACTION_MOVE " + yDiff);
                //如果正在下拉刷新并向上滑动，消费触摸事件
                if((mProgressBar.isRunning() && yDiff < 0) && mTarget.getTop() > 1){
                    int yTargetTop = (int) Math.max(-mTarget.getTop(), yDiff);
                    setTargetOffsetTopAndBottom(yTargetTop);
                    runningMove = true;
//                    if (mProgressBar.isRunning())
//                        mProgressBar.stop();
//                    if(mProgressBarBottom.isRunning())
//                        mProgressBarBottom.stop();
                }
                ///如果正在上拉加载并向下滑动，消费触摸事件
                if((mProgressBarBottom.isRunning() && yDiff > 0) && mTarget.getTop() < 0){
                    int yTargetTop = (int) Math.min(-mTarget.getTop(), yDiff);
                    setTargetOffsetTopAndBottom(yTargetTop);
                    runningMove = true;
                }
                //若上个手势的方向和当前手势方向不一致，返回
                if((mLastDirection == Mode.PULL_FROM_START && yDiff < 0) ||
                        (mLastDirection == Mode.PULL_FROM_END && yDiff > 0))
                {
                    return false;
                }
                //下拉或上拉时，子控件本身能够滑动时，记录当前手指位置，当其滑动到尽头时，
                //mStartPoint作为下拉刷新或上拉加载的手势起点
                if ((scrollAble.canChildScrollUp(ev.getRawX(), mStartLocaY + yDiff) && yDiff > 0) || (scrollAble.canChildScrollDown(ev.getRawX(), mStartLocaY + yDiff) && yDiff < 0))
                {
                    mStartPoint = y;
                }

                //下拉
                if (yDiff > mTouchSlop)
                {
                    //若当前子控件能向下滑动，或者上个手势为上拉，则返回
                    if (scrollAble.canChildScrollUp(ev.getRawX(), mStartLocaY + yDiff) || mLastDirection == Mode.PULL_FROM_END || !canRefresh)
                    {
                        mIsBeingDragged = false;
                        return false;
                    }
                    if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH))
                    {
                        mLastMotionY = y;
                        mIsBeingDragged = true;
                        mLastDirection = Mode.PULL_FROM_START;
                    }
                }
                //上拉
                else if (-yDiff > mTouchSlop) {
                    //若当前子控件能向上滑动，或者上个手势为下拉，则返回
                    if (scrollAble.canChildScrollDown(ev.getRawX(), mStartLocaY + yDiff) || mLastDirection == Mode.PULL_FROM_START || !canLoadMore)
                    {
                        mIsBeingDragged = false;
                        return false;
                    }
                    if ((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH))
                    {
                        mLastMotionY = y;
                        mIsBeingDragged = true;
                        mLastDirection = Mode.PULL_FROM_END;
                    }
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mActivePointerId = INVALID_POINTER;
                mLastDirection = Mode.DISABLED;
                if(runningMove){
                    updatePositionTimeout();
                    return false;
                }
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // Nope.
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

//        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
//            mReturningToStart = false;
//        }

        if (!isEnabled() && mReturningToStart) {
            // Fail fast if we're not in a state where a swipe is possible
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                canUpLoading = false;
                canDownLoading = false;
                mCurrPercentage = 0;
                mStartPoint = mInitialMotionY;
                mStartLocaY = ev.getRawY();
                mToucheventTargetOffsetTop = mCurrentTargetOffsetTop;

                up = scrollAble.canChildScrollUp(ev.getRawX(), ev.getRawY());
                down = scrollAble.canChildScrollDown(ev.getRawX(), ev.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
//                final float yDiff = y - mInitialMotionY;
                float yDiff = (y - mStartPoint) * TOUCH_MOVE_FLAG;
                if(mToucheventTargetOffsetTop >= mProgressBarHeight && mProgressBar.isRunning()){
                    yDiff += mProgressBarHeight;
                } else if(mToucheventTargetOffsetTop <= -mProgressBarHeightBottom && mProgressBarBottom.isRunning()){
                    yDiff -= mProgressBarHeightBottom;
                }

                if((mLastDirection == Mode.PULL_FROM_START && yDiff < 0) ||
                        (mLastDirection == Mode.PULL_FROM_END && yDiff > 0))
                {
                    return true;
                }

                if (!mIsBeingDragged && (yDiff > 0 && mLastDirection == Mode.PULL_FROM_START)
                        || (yDiff < 0 && mLastDirection == Mode.PULL_FROM_END)) {
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    // User velocity passed min velocity; trigger a refresh
                    if (yDiff >= mDistanceToTriggerSync && !mProgressBar.isRunning() && !mProgressBarBottom.isRunning()) {//当手指下拉距离超过阈值并且当前不在刷新中
                        // User movement passed distance; trigger a refresh
                        if(mLastDirection == Mode.PULL_FROM_END)
                        {
                            return true;

                        }
                        if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH))
                        {
                            mLastDirection = Mode.PULL_FROM_START;
                            canUpLoading = true;
                        }
                    } else if (-yDiff >= mDistanceToTriggerSync * .5f && !mProgressBarBottom.isRunning() && !mProgressBar.isRunning()) {//当手指上拉距离超过阈值并且当前不在加载中
                        if(mLastDirection == Mode.PULL_FROM_START)
                        {
                            return true;
                        }
                        if ((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH))
                        {
                            mLastDirection = Mode.PULL_FROM_END;
                            canDownLoading = true;
                        }
                    }else{
                        if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH))
                        {
                            canUpLoading = false;
                        }
                        if((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH)){
                            canDownLoading = false;
                        }
                    }

                    // Just track the user's movement
                    //根据手指移动距离设置进度条显示的百分比
                    setTriggerPercentage(yDiff, mDistanceToTriggerSync);
                    if((!scrollAble.canChildScrollDown(ev.getRawX(), mStartLocaY + yDiff) && !mProgressBar.isRunning()) || (!scrollAble.canChildScrollUp(ev.getRawX(), mStartLocaY + yDiff) && !mProgressBarBottom.isRunning()))
                        updateContentOffsetTop((int)yDiff);
                    if (mTarget.getTop() == getPaddingTop()) {
                        // If the user puts the view back at the top, we
                        // don't need to. This shouldn't be considered
                        // cancelling the gesture as the user can restart from the top.
                        removeCallbacks(mCancel);
                        mLastDirection = Mode.DISABLED;
                    }

                    mLastMotionY = y;
                }
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionY = MotionEventCompat.getY(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                refreshingToStop = canUpLoading;
                loadingToStop = canDownLoading;
                if(canUpLoading)
                    startRefresh();
                else if(canDownLoading)
                    startLoad();
                else
                    updatePositionTimeout();
                canUpLoading = false;
                canDownLoading = false;
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mActivePointerId = INVALID_POINTER;
                mLastDirection = Mode.DISABLED;
                return false;
        }

        return true;
    }

    private void startRefresh() {
        if (!mLoading && !mRefreshing)
        {
            removeCallbacks(mCancel);
            setRefreshing(true);
            if(mRefreshListener != null)
                mRefreshListener.onRefresh();
        }
    }

    private void startLoad() {
        if (!mLoading && !mRefreshing)
        {
            removeCallbacks(mCancel);
            setLoading(true);
            if(mLoadListener != null)
                mLoadListener.onLoad();
        }
    }

    //手指移动时更新子控件的位置
    private void updateContentOffsetTop(int targetTop) {
        final int currentTop = mTarget.getTop();
        //注释掉，不然下拉到阈值时子控件无法继续下拉
        if (targetTop > mDistanceToTriggerSync * 1.6f) {
            targetTop = (int) (mDistanceToTriggerSync * 1.6f);
        }
        if(-targetTop > mDistanceToTriggerSync * 1.6f)
            targetTop = -(int) (mDistanceToTriggerSync * 1.6f);
        //取消上一次动画，防止对下次子控件位置设置动画产生影响
        mTarget.clearAnimation();
        setTargetOffsetTopAndBottom(targetTop - currentTop);
    }

    //根据偏移量对子控件进行移动
    private void setTargetOffsetTopAndBottom(int offset) {
        mTarget.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mTarget.getTop();
    }

    private void updatePositionTimeout() {
        removeCallbacks(mCancel);
        post(mCancel);
//        postDelayed(mCancel, RETURN_TO_ORIGINAL_POSITION_TIMEOUT);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = MotionEventCompat.getY(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    /**
     * 如果子控件是自己定义的控件，并存在滑动冲突的，请自行实现该接口解决
     * @param scrollAble
     */
    public void setScrollAble(SwipeRefreshScrollInterface scrollAble){
        this.scrollAble = scrollAble;
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public interface OnLoadListener {
        public void onLoad();
    }

    public void setMode(Mode mode)
    {
        this.mMode = mode;
    }

    public void setLoadNoFull(boolean load)
    {
        this.loadNoFull = load;
    }

    public void setCanRefresh(boolean canRefresh){
        this.canRefresh = canRefresh;
    }

    public void setCanLoadMore(boolean canLoadMore){
        this.canLoadMore = canLoadMore;
    }

    public static enum Mode {
        /**
         * Disable all Pull-to-Refresh gesture and Refreshing handling
         */
        DISABLED(0x0),

        /**
         * Only allow the user to Pull from the start of the Refreshable View to
         * refresh. The start is either the Top or Left, depending on the
         * scrolling direction.
         */
        PULL_FROM_START(0x1),

        /**
         * Only allow the user to Pull from the end of the Refreshable View to
         * refresh. The start is either the Bottom or Right, depending on the
         * scrolling direction.
         */
        PULL_FROM_END(0x2),

        /**
         * Allow the user to both Pull from the start, from the end to refresh.
         */
        BOTH(0x3);

        static Mode getDefault() {
            return BOTH;
        }

        boolean permitsPullToRefresh() {
            return !(this == DISABLED);
        }
        boolean permitsPullFromStart() {
            return (this == Mode.BOTH || this == Mode.PULL_FROM_START);
        }
        boolean permitsPullFromEnd() {
            return (this == Mode.BOTH || this == Mode.PULL_FROM_END);
        }

        private int mIntValue;

        // The modeInt values need to match those from attrs.xml
        Mode(int modeInt) {
            mIntValue = modeInt;
        }

        int getIntValue() {
            return mIntValue;
        }

    }

    /**
     * Simple AnimationListener to avoid having to implement unneeded methods in
     * AnimationListeners.
     */
    private class BaseAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}

