package com.leconssoft.common.widgt;

import android.content.Context;
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
import com.leconssoft.common.widgt.swiprefresh.BaseSwipProgressBar;
import com.leconssoft.common.widgt.swiprefresh.LoadMoreProgressBar;
import com.leconssoft.common.widgt.swiprefresh.LogoProgressBar;
import com.leconssoft.common.widgt.swiprefresh.intf.SwipeRefreshScrollInterface;


/**
 * <p>类说明</p>
 * 可以滑动整体布局刷新
 *
 * @author yucheng
 * @date 2018/11/14 10:45
 * @Description
 */
public class RefreshAllLayout extends ViewGroup implements SwipeRefreshScrollInterface {
    private static final String LOG_TAG = RefreshAllLayout.class.getSimpleName();
    //无效的指针ID
    private static final int INVALID_POINTER = -1;
    //刷新距离
    private static final int REFRESH_TRIGGER_DISTANCE = 40;
    //触摸移动的标记
    private static final float TOUCH_MOVE_FLAG = .45f;
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 2f;
    private static final float PROGRESS_BAR_HEIGHT = 60;
    private static final float PROGRESS_BAR_HEIGHT_BOTTOM = 30;
    public BaseSwipProgressBar topBar;
    public BaseSwipProgressBar bottomBar;
    //被下拉的内容 滑动的布局
    private View mTarget;
    //刷新的进度框的高度
    private int mProgressBarHeight;
    //topBar控件高度
    private int mCurrentTargetOffsetTop;
    private int mProgressBarHeightBottom;

    private int fullViewTop;//整个布局的高度

    private float mDistanceToTriggerSync = -1;//下拉/上拉的阈值，当超过这个距离时，松开手指开始加载数据

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


    // 目标正在返回它的起始偏移量，因为它被取消或a
    // 刷新被触发
    private boolean mReturningToStart;

    //记录touch事件的指针
    private int mActivePointerId;

    //最初始的Y值
    private float mInitialMotionY;
    //滑动后的Y值，滑动的距离
    private float mLastMotionY;
    //是划动状态
    private boolean mIsBeingDragged;

    //当子控件移动到尽头时才开始计算初始点的位置
    private float mStartPoint;
    //初始点X值
    private float mStartPointX;
    //移动前原始Y值
    private float mStartLocaY;
    // 回复进度百分比
    private float mCurrPercentage = 0;
    //目标移动距离
    private float mToucheventTargetOffsetTop;
    // 方向状态
    private boolean up;
    private boolean down;
    //是否在滑动
    private boolean runningMove = false;
    //移动方向
    private int mTouchSlop;

    //之前手势的方向，为了解决同一个触点前后移动方向不同导致后一个方向会刷新的问题，
    private Mode mMode = Mode.getDefault();
    //这里Mode.DISABLED无意义，只是一个初始值，和上拉/下拉方向进行区分
    private Mode mLastDirection = Mode.DISABLED;

    //是否操作刷新的距离
    private boolean isOutRefreshLine = false;
    //动画持续时间
    private int mMediumAnimationDuration=1000;
    //减速度插值器
    private DecelerateInterpolator mDecelerateInterpolator;
    //加速
    private AccelerateInterpolator mAccelerateInterpolator;

    //是否刷新中
    private boolean mRefreshing = false;
    private boolean mLoading = false;
    //进度加载停止状态
    private boolean refreshingToStop = false;
    private boolean loadingToStop = false;
    private int mFrom;

    private OnRefreshListener mRefreshListener;
    private OnLoadListener mLoadListener;
    private SwipeRefreshScrollInterface slidingEventInterface;

    private static final int[] LAYOUT_ATTRS = new int[]{android.R.attr.enabled};

    public RefreshAllLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //包含UI中用于超时、大小和距离的标准常量的方法
        //getScaledTouchSlop()在我们认为用户在滚动之前，触摸的距离(以像素为单位)可能会走散
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mDecelerateInterpolator = new DecelerateInterpolator(ACCELERATE_INTERPOLATION_FACTOR);
        mAccelerateInterpolator = new AccelerateInterpolator(ACCELERATE_INTERPOLATION_FACTOR);
        //如果我们想要重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false);
        slidingEventInterface = this;
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        topBar = new LogoProgressBar(this, Utils.dip2px(context, 24), Utils.sp2px(context, 3));
        bottomBar = new LoadMoreProgressBar(this, Utils.sp2px(context, 13));
        mProgressBarHeight = (int) (metrics.density * PROGRESS_BAR_HEIGHT);
        mProgressBarHeightBottom = (int) (metrics.density * PROGRESS_BAR_HEIGHT_BOTTOM);
        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //获取View测量高度
        final int viewWidth = getMeasuredWidth();
        final int viewHeigth = getMeasuredHeight();
        //设置进度框的宽高
        topBar.setBounds(0, 0, viewWidth, mProgressBarHeight);
        //验证是否有子布局
        if (getChildCount() == 0) {
            return;
        }
        //设置头布局的起始位置
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = mCurrentTargetOffsetTop + getPaddingTop();
        final int childWidth = viewWidth - getPaddingLeft() - getPaddingRight();
        final int childHeight = viewHeigth - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        //绘制加载更多进度框的 坐标
        bottomBar.setBounds(0, viewHeigth - mProgressBarHeightBottom, viewWidth, viewHeigth);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canRefresh) {
            topBar.draw(canvas);
        }
        if (canLoadMore) {
            bottomBar.draw(canvas);
        }
        super.draw(canvas);
    }

    public void setScrollAble(SwipeRefreshScrollInterface slidingEventInterface) {
        this.slidingEventInterface = slidingEventInterface;
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setOnRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }


    public void  setLoadListener(OnLoadListener mLoadListener) {
        this.mLoadListener = mLoadListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException("只能承载一个直接子节点");
        }
        if (getChildCount() > 0) {
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        }
    }


    /***
     * 是用于处理事件（重点onInterceptTouchEvent这个事件是从父控件开始往子控件传的，
     * 直到有拦截或者到没有这个事件的view，然后就往回从子到父控件，这次是onTouch的）
     * （类似于预处理，当然也可以不处理）并改变事件的传递方向，
     * 也就是决定是否允许Touch事件继续向下（子控件）传递，
     * 一但返回True（代表事件在当前的viewGroup中会被处理），
     * 则向下传递之路被截断（所有子控件将没有机会参与Touch事件），
     * 同时把事件传递给当前的控件的onTouchEvent()处理；
     * 返回false，则把事件交给子控件的onInterceptTouchEvent()
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //这里测量滑动布局的高度
        ensureTarget();
        final int action = MotionEventCompat.getActionMasked(ev);
        if (!isEnabled() && mReturningToStart) {
            // 如果在不可以滑动的状态
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                //初始 Y值
                mLastMotionY = mInitialMotionY = ev.getY();
                mStartPointX = ev.getX();
                //初始的PointerId
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mStartPoint = mInitialMotionY;
                //返回此事件的原始Y坐标
                mStartLocaY = ev.getRawY();
                //是否在滑动
                runningMove = false;
                //目标移动距离
                mToucheventTargetOffsetTop = mCurrentTargetOffsetTop;
                up = slidingEventInterface.canChildScrollUp(ev.getRawX(), ev.getRawY());
                down = slidingEventInterface.canChildScrollDown(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                //判断移动事件的touchId是否有获取到
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "获取ACTION_MOVE事件，但没有活动指针id.");
                    return false;
                }
                //获取mActivePointerId的指针下标
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                //如果下标小于0这个事件就是无效的事件指针
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "获取ACTION_MOVE事件，但有一个无效的活动指针id.");
                    return false;
                }
                //获取事件的X,Y值
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float x = MotionEventCompat.getX(ev, pointerIndex);

                final float yDiff = (y - mStartPoint) * TOUCH_MOVE_FLAG;
                final float xDiff = (x - mStartPointX) * TOUCH_MOVE_FLAG;

                //手指垂直滑动角度小于30°时不判断为垂直滑动
                if (Math.abs(yDiff) / Math.abs(xDiff) <= Math.tan(Math.toRadians(30))) {
                    mIsBeingDragged = false;
                    return false;
                }
                Log.d("NestedScroll", "onInterceptTouchEvent ACTION_MOVE " + yDiff);
                //如果正在下拉刷新并向上滑动，消费触摸事件
                if ((topBar.isRunning() && yDiff < 0) && mTarget.getTop() > 1) {
                    int yTargetTop = (int) Math.max(-mTarget.getTop(), yDiff);
                    setTargetOffsetTopAndBottom(yTargetTop);
                    runningMove = true;
                }
                ///如果正在上拉加载并向下滑动，消费触摸事件
                if ((bottomBar.isRunning() && yDiff > 0) && mTarget.getTop() < 0) {
                    int yTargetTop = (int) Math.min(-mTarget.getTop(), yDiff);
                    setTargetOffsetTopAndBottom(yTargetTop);
                    runningMove = true;
                }
                //若上个手势的方向和当前手势方向不一致，返回
                if ((mLastDirection == Mode.PULL_FROM_START && yDiff < 0) || (mLastDirection == Mode.PULL_FROM_END && yDiff > 0)) {
                    return false;
                }
                //下拉或上拉时，子控件本身能够滑动时，记录当前手指位置，当其滑动到尽头时，
                //mStartPoint作为下拉刷新或上拉加载的手势起点
                if ((slidingEventInterface.canChildScrollUp(ev.getRawX(), mStartLocaY + yDiff) && yDiff > 0) || (slidingEventInterface.canChildScrollDown(ev.getRawX(), mStartLocaY + yDiff) && yDiff < 0)) {
                    mStartPoint = y;
                }

                //下拉
                if (yDiff > mTouchSlop) {
                    //若当前子控件能向下滑动，或者上个手势为上拉，则返回
                    if (slidingEventInterface.canChildScrollUp(ev.getRawX(), mStartLocaY + yDiff) || mLastDirection == Mode.PULL_FROM_END || !canRefresh) {
                        mIsBeingDragged = false;
                        return false;
                    }
                    if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH)) {
                        mLastMotionY = y;
                        mIsBeingDragged = true;
                        mLastDirection = Mode.PULL_FROM_START;
                    }
                }
                //上拉
                else if (-yDiff > mTouchSlop) {
                    //若当前子控件能向上滑动，或者上个手势为下拉，则返回
                    if (slidingEventInterface.canChildScrollDown(ev.getRawX(), mStartLocaY + yDiff) || mLastDirection == Mode.PULL_FROM_START || !canLoadMore) {
                        mIsBeingDragged = false;
                        return false;
                    }
                    if ((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH)) {
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
                if (runningMove) {
                    updatePositionTimeout();
                    return false;
                }
                break;
        }
        return mIsBeingDragged;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (!isEnabled() && mReturningToStart) {
            // 如果我们不在一个可以滑动的状态，那么快速失败
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //起始滑动距离
                mLastMotionY = mInitialMotionY = event.getY();
                //获取点击事件 id
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                //状态重置
                mIsBeingDragged = false;
                canUpLoading = false;
                canDownLoading = false;
                mCurrPercentage = 0;
                //初始点的位置
                mStartPoint = mInitialMotionY;
                mStartLocaY = event.getRawY();
                //目标移动的距离=进度控件的距离
                mToucheventTargetOffsetTop = mCurrentTargetOffsetTop;
                //方向判断
                up = slidingEventInterface.canChildScrollUp(event.getRawX(), event.getRawY());
                down = slidingEventInterface.canChildScrollDown(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(event, pointerIndex);
                float yDiff = (y - mStartPoint) * TOUCH_MOVE_FLAG;
                if (mToucheventTargetOffsetTop >= mProgressBarHeight && topBar.isRunning()) {
                    yDiff += mProgressBarHeight;
                } else if (mToucheventTargetOffsetTop <= -mProgressBarHeightBottom && bottomBar.isRunning()) {
                    yDiff -= mProgressBarHeightBottom;
                }

                if ((mLastDirection == Mode.PULL_FROM_START && yDiff < 0) || (mLastDirection == Mode.PULL_FROM_END && yDiff > 0)) {
                    return true;
                }

                if (!mIsBeingDragged && (yDiff > 0 && mLastDirection == Mode.PULL_FROM_START) || (yDiff < 0 && mLastDirection == Mode.PULL_FROM_END)) {
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    //当手指下拉距离超过阈值并且当前不在刷新中
                    if (yDiff >= mDistanceToTriggerSync && !topBar.isRunning() && !bottomBar.isRunning()) {
                        // User movement passed distance; trigger a refresh
                        if (mLastDirection == Mode.PULL_FROM_END) {
                            return true;

                        }
                        if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH)) {
                            mLastDirection = Mode.PULL_FROM_START;
                            canUpLoading = true;
                        }
                    } else if (-yDiff >= mDistanceToTriggerSync * .5f && !bottomBar.isRunning() && !topBar.isRunning()) {
                        //当手指上拉距离超过阈值并且当前不在加载中
                        if (mLastDirection == Mode.PULL_FROM_START) {
                            return true;
                        }
                        if ((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH)) {
                            mLastDirection = Mode.PULL_FROM_END;
                            canDownLoading = true;
                        }
                    } else {
                        if ((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH)) {
                            canUpLoading = false;
                        }
                        if ((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH)) {
                            canDownLoading = false;
                        }
                    }
                    //根据手指移动距离设置进度条显示的百分比
                    setTriggerPercentage(yDiff, mDistanceToTriggerSync);
                    if ((!slidingEventInterface.canChildScrollDown(event.getRawX(), mStartLocaY + yDiff) && !topBar.isRunning()) || (!slidingEventInterface.canChildScrollUp(event.getRawX(), mStartLocaY + yDiff) && !bottomBar.isRunning())) {
                        updateContentOffsetTop((int) yDiff);
                    }
                    if (mTarget.getTop() == getPaddingTop()) {
                        //如果用户将视图放回顶部，我们
                        // 不需要。这是不应该考虑的
                        // 取消手势，因为用户可以从顶部重新启动。
                        removeCallbacks(mCancel);
                        mLastDirection = Mode.DISABLED;
                    }

                    mLastMotionY = y;
                }
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(event);
                mLastMotionY = MotionEventCompat.getY(event, index);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                refreshingToStop = canUpLoading;
                loadingToStop = canDownLoading;
                if (canUpLoading) {
                    startRefresh();
                } else if (canDownLoading) {
                    startLoad();
                } else {
                    updatePositionTimeout();
                }
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

    /**
     * 确定布局里面有控件可以测量设置
     */
    private void ensureTarget() {
        // 如果没有下拉的界面就不必要去设置刷新.
        if (mTarget == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
            }
            mTarget = getChildAt(0);
            if (mTarget instanceof RecyclerView && canLoadMore) {
                ((RecyclerView) mTarget).addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //当前屏幕显示的区域高度
                        int screenHeight = ((RecyclerView) mTarget).computeVerticalScrollExtent();
                        //整个RecycleView控件的高度
                        int totalHeight = ((RecyclerView) mTarget).computeVerticalScrollRange();
                        //当前屏幕之前滑过的距离
                        int scrollOffSet = ((RecyclerView) mTarget).computeVerticalScrollOffset();
                        //当recycleView滑动到底部时 screenHeight + scrollOffSet = totalHeight
                        if (totalHeight - scrollOffSet <= (float) screenHeight * 1.5f && !bottomBar.isRunning() && canLoadMore) {
                            //判断是否执行加载更多
                        }
                    }
                });
            }
            //布局高度+间距高度=整体布局的高度
            fullViewTop = mTarget.getTop() + getPaddingTop();
        }
        //设置下拉/上拉阈值
        if (mDistanceToTriggerSync == -1) {
            if (getParent() != null && ((View) getParent()).getHeight() > 0) {
                //获取分辨率
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToTriggerSync =REFRESH_TRIGGER_DISTANCE * metrics.density;
            }
        }
    }

    /**
     * 二次手势指针设置
     *
     * @param ev
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        //手势起始点
        //通过getPointerId()获得ID，这样就可以在多个motion event中追踪pointer
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // 这是我们向上的活动指针。选择一个新的
            // 活动指针和相应的调整。
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = MotionEventCompat.getY(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    //根据偏移量对子控件进行移动
    private void setTargetOffsetTopAndBottom(int offset) {
        mTarget.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mTarget.getTop();
    }

    @Override
    public boolean canChildScrollUp(float x, float y) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {

            //direction参数为要判断的方向：
            //小于0：是否可以向下拉（对应向上滚）
            //大于等于0：是否可以向上拉（对应向下滚）
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    @Override
    public boolean canChildScrollDown(float x, float y) {
        if (android.os.Build.VERSION.SDK_INT < 14) {//SDK14以下暂不做支持
            return false;
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }


    public static enum Mode {
        /**
         * 禁用所有的下拉刷新手势和刷新处理
         */
        DISABLED(0x0),

        /**
         * 只允许用户从Refreshable视图开始拉到*刷新。
         * 起始点是顶部还是左侧，
         * 这取决于滚动方向。
         */
        PULL_FROM_START(0x1),

        /**
         * 只允许用户从Refreshable视图的末尾拉到
         * 刷新。起始点要么在底部，
         * 要么在右边，这取决于滚动方向。
         */
        PULL_FROM_END(0x2),

        /**
         * 允许用户从开始，从结束，到刷新。
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

    private void updatePositionTimeout() {
        removeCallbacks(mCancel);
        post(mCancel);
//        postDelayed(mCancel, RETURN_TO_ORIGINAL_POSITION_TIMEOUT);
    }

    //取消刷新手势，并将一切动画恢复到原来的状态。
    //取消刷新手势并将子控件状态重置
    private final Runnable mCancel = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            isOutRefreshLine = mCurrentTargetOffsetTop >= mProgressBarHeight || mCurrentTargetOffsetTop <= -mProgressBarHeight;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
        }

    };

    //对子控件进行移动
    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
        mAnimateToStartPosition.setAnimationListener(listener);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mTarget.startAnimation(mAnimateToStartPosition);
    }

    //对下拉或上拉进行复位 动画
    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            // TODO: 2018/11/15  设置动画效果
            int targetTop = 0;
            int mTempOriginalOffsetTop = fullViewTop;
            if (isOutRefreshLine) {//是否超过加载阈值
                if (topBar.isRunning()) {
                    mTempOriginalOffsetTop += mProgressBarHeight;
                }
                if (bottomBar.isRunning()) mTempOriginalOffsetTop -= mProgressBarHeightBottom;
            }
            if (mFrom != mTempOriginalOffsetTop) {
                targetTop = (mFrom + (int) ((mTempOriginalOffsetTop - mFrom) * interpolatedTime));
            }
            int offset = targetTop - mTarget.getTop();
            if (Math.abs(targetTop) < Math.abs(mTempOriginalOffsetTop * .2) || targetTop == 0)
                mReturningToStart = false;
            setTriggerPercentage(targetTop, mDistanceToTriggerSync);
            setTargetOffsetTopAndBottom(offset);
        }
    };

    //监听，回复初始位置
    private final Animation.AnimationListener mReturnToStartPositionListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // 目标内容返回到起始位置后，
            // 重置目标偏移量mCurrentTargetOffsetTop为0
            mCurrentTargetOffsetTop = 0;
            if (topBar.isRunning()) {
                mCurrentTargetOffsetTop = mProgressBarHeight;
            }
            if (bottomBar.isRunning()) {
                mCurrentTargetOffsetTop = mProgressBarHeightBottom;
            }
            mLastDirection = Mode.DISABLED;
            mReturningToStart = false;
            refreshingToStop = false;
            loadingToStop = false;
            mLoading = false;
            mRefreshing = false;
            topBar.stop();
            bottomBar.stop();
        }
    };


    /**
     * 简单的AnimationListener，以避免在其中实现不必要的方法
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

    //设置进度条的显示百分比
    private void setTriggerPercentage(float currentDiff, float total) {
        float percent = mAccelerateInterpolator.getInterpolation(Math.min(total, Math.abs(currentDiff)) / total);
        if (percent == 0f) {
            // No-op. A null trigger means it's uninitialized, and setting it to zero-percent
            // means we're trying to reset state, so there's nothing to reset in this case.
            mCurrPercentage = 0;
            return;
        }
        mCurrPercentage = percent;
        if (((mMode == Mode.PULL_FROM_START) || (mMode == Mode.BOTH)) && mLastDirection != Mode.PULL_FROM_END && !mLoading && !refreshingToStop) {
            //添加百分比
            topBar.setTriggerPercentage((int) percent);
        } else if (((mMode == Mode.PULL_FROM_END) || (mMode == Mode.BOTH)) && mLastDirection != Mode.PULL_FROM_START && !mRefreshing && !loadingToStop) {
            percent = mAccelerateInterpolator.getInterpolation(Math.min(total * .5f, Math.abs(currentDiff)) / (total * .5f));
            bottomBar.setTriggerPercentage((int) percent);
        }
        mCurrPercentage = percent;
    }

    //手指移动时更新子控件的位置
    private void updateContentOffsetTop(int targetTop) {
        final int currentTop = mTarget.getTop();
        //注释掉，不然下拉到阈值时子控件无法继续下拉
        if (targetTop > mDistanceToTriggerSync * 1.6f) {
            targetTop = (int) (mDistanceToTriggerSync * 1.6f);
        }
        if (-targetTop > mDistanceToTriggerSync * 1.6f)
            targetTop = -(int) (mDistanceToTriggerSync * 1.6f);
        //取消上一次动画，防止对下次子控件位置设置动画产生影响
        mTarget.clearAnimation();
        setTargetOffsetTopAndBottom(targetTop - currentTop);
    }

    private void startRefresh() {
        if (!mLoading && !mRefreshing) {
            removeCallbacks(mCancel);
            setRefreshing(true);
            if (mRefreshListener != null) mRefreshListener.onRefresh();
        }
    }

    private void startLoad() {
        if (!mLoading && !mRefreshing) {
            removeCallbacks(mCancel);
            setLoading(true);
            if (mLoadListener != null) mLoadListener.onLoad();
        }
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
                if (mCurrentTargetOffsetTop == getPaddingTop() && !topBar.isRunning()) {
                    isOutRefreshLine = true;
                }
                topBar.start();
                mReturnToStartPosition.run();
            } else {
                mLastDirection = Mode.DISABLED;
                // TODO: 2018/11/15  这里要写一个topBar进度控件停止的方法
                topBar.stop();
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
                bottomBar.start();
                mReturnToStartPosition.run();
            } else {
                mLastDirection = Mode.DISABLED;
                // TODO: 2018/11/15  这里要写一个bottomBar进度控件停止的方法
                bottomBar.stop();
            }
        }
    }

    //回复初始位置
    private final Runnable mReturnToStartPosition = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
        }

    };

    /**
     * 刷新回调
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadListener {
        void onLoad();
    }

}
