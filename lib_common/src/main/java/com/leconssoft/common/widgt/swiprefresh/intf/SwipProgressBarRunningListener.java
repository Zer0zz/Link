package com.leconssoft.common.widgt.swiprefresh.intf;

/**
 * 进度加载监听
 * Created by GYH on 2017/10/31.
 */

public interface SwipProgressBarRunningListener {

    //real finish:  mRunning = false && mFinishTime = 0
    void onRealFinish();

    void onStart();
}
