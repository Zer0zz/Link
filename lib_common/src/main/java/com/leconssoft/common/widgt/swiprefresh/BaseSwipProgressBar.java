package com.leconssoft.common.widgt.swiprefresh;

import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.leconssoft.common.widgt.swiprefresh.intf.SwipProgressBarRunningListener;
import com.leconssoft.common.widgt.swiprefresh.intf.SwipeProgressInterface;


/**
 * Created by GYH on 2017/10/31.
 */

public abstract class BaseSwipProgressBar implements SwipeProgressInterface {
    protected View mParent;

    /**
     * 动画插值器
     */
    protected Interpolator INTERPOLATOR;
    /**
     * 画笔
     */
    protected final Paint mPaint = new Paint();
    /**
     * 进度
     */
    protected float mTriggerPercentage;
    /**
     * 动画开始时间
     */
    protected long mStartTime;
    /**
     * 动画结束时间
     */
    protected long mFinishTime;
    /**
     * 是否正在加载
     */
    protected boolean mRunning;

    /**
     * 绘制区域
     */
    protected Rect mBounds = new Rect();

    /**
     * 加载监听
     */
    protected SwipProgressBarRunningListener runningListener;

    public BaseSwipProgressBar(View mParent) {
        this.mParent = mParent;
    }

    @Override
    public void start() {
        if (!mRunning) {
            mTriggerPercentage = 0;
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = true;
            mParent.postInvalidate();
        }
    }

    @Override
    public void stop() {
        if (mRunning) {
            mTriggerPercentage = 0;
//            mFinishTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = false;
//            mParent.postInvalidate();
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;
    }

    @Override
    public boolean isRunning() {
        //是否加载由mRunning 和 mFinishTime 决定;
//        return mRunning || mFinishTime > 0;
        //现在是否加载由mRunning决定，因为加载视图的绘制顺序做了修改，改为在原有内容之前先绘制。按照之前的逻辑 调用stop方法后实际并没有正真结束，还会刷新界面绘制最后一轮，所以会导致整个界面的闪烁
        return mRunning;
    }

    @Override
    public void setTriggerPercentage(float triggerPercentage) {
        mTriggerPercentage = triggerPercentage;
        mStartTime = 0;
        ViewCompat.postInvalidateOnAnimation(mParent);
    }

    @Override
    public void setRunningListener(SwipProgressBarRunningListener runningListener) {
        this.runningListener = runningListener;
    }
}
