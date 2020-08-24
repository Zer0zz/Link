package com.leconssoft.common.widgt.swiprefresh;

import android.graphics.Canvas;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.leconssoft.common.R;

/**
 * Created by GYH on 2017/11/9.
 */

public class LoadMoreProgressBar extends BaseSwipProgressBar {

    private String pushMsg = "上拉加载";
    private String releaseMsg = "松开加载";
    private String loadingMsg = "加载中...";

    public LoadMoreProgressBar(View mParent, int textSize) {
        super(mParent);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(mParent.getResources().getColor(R.color.black));
    }

    @Override
    public void draw(Canvas canvas) {
        final int width = mBounds.width();
        final int height = mBounds.height();
        final float textWidth = mPaint.measureText(pushMsg);
        int restoreCount = canvas.save();
        canvas.clipRect(mBounds);

        if(mRunning){
            long now = AnimationUtils.currentAnimationTimeMillis();
            long elapsed = (now - mStartTime) % ANIMATION_DURATION_MS_SHORT;
            int floor = (int) Math.floor((now - mStartTime) / (float)ANIMATION_DURATION_MS_SHORT);
            float loadProgress = elapsed / (float)ANIMATION_DURATION_MS_SHORT;
            int progressLum = floor%2 == 0 ? (int) (60 - loadProgress * 120) : (int) (60 - (1-loadProgress) * 120);
            canvas.drawText(loadingMsg, (width - textWidth) / 2, mBounds.bottom - height / 2, mPaint);
            ViewCompat.postInvalidateOnAnimation(mParent);
        }else if(mTriggerPercentage > 0 && mTriggerPercentage < 1.0){
            canvas.drawText(pushMsg, (width - textWidth) / 2, mBounds.bottom - height / 2, mPaint);
        } else if(mTriggerPercentage == 1.0){
            canvas.drawText(releaseMsg, (width - textWidth) / 2, mBounds.bottom - height / 2, mPaint);
        }

        canvas.restoreToCount(restoreCount);
    }

    @Override
    public void stop() {
        super.stop();
        if(runningListener != null)
            runningListener.onRealFinish();
    }

    @Override
    public void setColorScheme(int... colors) {

    }
}
