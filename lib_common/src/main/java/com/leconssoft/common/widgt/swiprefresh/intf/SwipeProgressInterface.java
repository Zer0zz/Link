package com.leconssoft.common.widgt.swiprefresh.intf;

import android.graphics.Canvas;

/**
 * SwipeRefreshLayout中使用的progressBar抽象接口
 * Created by GYH on 2017/10/31.
 */

public interface SwipeProgressInterface {
    /**
     * 进度动画循环间隔时间
     */
    int ANIMATION_DURATION_MS_NORMAL = 2000;
    int ANIMATION_DURATION_MS_SHORT = 1500;
    int ANIMATION_DURATION_MS_LONG = 3500;

    /**
     * 进度动画结束缓冲时间
     */
    int FINISH_ANIMATION_DURATION_MS = 1000;

    /**
     *  开始加载进度
     */
    void start();

    /**
     * 停止加载进度
     */
    void stop();

    /**
     * 进度动画绘制区域设置
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void setBounds(int left, int top, int right, int bottom);

    /**
     * 进度动画绘制
     * @param canvas  画布
     */
    void draw(Canvas canvas);

    /**
     * 是否正在加载
     * @return
     */
    boolean isRunning();

    /**
     * 设置动画进度
     * @param triggerPercentage  进度
     */
    void setTriggerPercentage(float triggerPercentage);

    /**
     * 进度加载监听
     * @param runningListener
     */
    void setRunningListener(SwipProgressBarRunningListener runningListener);

    /**
     * 设置变换颜色
     * @param colors
     */
    void setColorScheme(int... colors);
}
