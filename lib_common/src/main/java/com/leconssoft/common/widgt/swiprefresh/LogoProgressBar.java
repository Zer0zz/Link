package com.leconssoft.common.widgt.swiprefresh;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;


import com.leconssoft.common.R;

import java.util.Arrays;

/**
 * Created by GYH on 2017/10/31.
 */

public class LogoProgressBar extends BaseSwipProgressBar {
    private static int LOGO_MARGINTOP = 60;
    private final static float LOGO_LINEWIDTH = 1.0f;
    private final static float LOGO_LINEWIDTH_SMALL = .5f;

    private int logoWidth;
    private int logoHeight;
    private int lineWidth;

    private int lineColor;

    private static Point[] shape;
    private static Point[] progressShape;

    public LogoProgressBar(View mParent, int logoHeight, int lineWidth) {
        super(mParent);
        this.logoHeight = logoHeight;
        this.lineWidth = (int) (lineWidth * LOGO_LINEWIDTH);
        this.logoWidth = (int) (Math.sin(Math.toRadians(60)) * logoHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void draw(Canvas canvas) {
        final int width = mBounds.width();
        final int height = mBounds.height();
        LOGO_MARGINTOP = Math.max((height - logoHeight), 0)/2;
        int restoreCount = canvas.save();
        canvas.clipRect(mBounds);

        if(mRunning || mFinishTime > 0){
            long now = AnimationUtils.currentAnimationTimeMillis();
            long elapsed = (now - mStartTime) % ANIMATION_DURATION_MS_SHORT;
            int floor = (int) Math.floor((now - mStartTime) / (float)ANIMATION_DURATION_MS_SHORT);
            float loadProgress = elapsed / (float)ANIMATION_DURATION_MS_SHORT;
            int progressLum = floor%2 == 0 ? (int) (60 - loadProgress * 120) : (int) (60 - (1-loadProgress) * 120);
            drawLines(canvas, R.color.color_0093dd);
            setLum(progressLum);
            drawOutProgress(canvas, R.color.white, floor, loadProgress);
//            if (!mRunning) {
//                // If the finish animation is done, don't draw anything, and
//                // don't repost.
//                if ((now - mFinishTime) >= FINISH_ANIMATION_DURATION_MS) {
//                    mFinishTime = 0;
//                    if(runningListener != null)
//                        runningListener.onRealFinish();
//                    return;
//                }
//            }
        } else if(mTriggerPercentage > 0 && mTriggerPercentage <= 1.0){
            drawLogoProgress(canvas, R.color.color_0093dd);
        }
        canvas.restoreToCount(restoreCount);
    }

    private void drawLogoProgress(Canvas canvas, int color){
        if(shape != null){
            mPaint.setColor(mParent.getResources().getColor(color));
            mPaint.setStrokeWidth(lineWidth);
            Path path = new Path();
            float pre_max = 1f/(float) (shape.length);
            float progress = mTriggerPercentage/pre_max;
            float lineProgress = progress%1f;
            int linePart = (int) Math.ceil(progress);

            for(int i = 0; i< Math.min(shape.length, linePart); i++){
                Point p = shape[i];
                if(i == 0 || i == 7 || i == 9)
                    path.moveTo(p.x, p.y);
                else{
                    //先移动后连线而不是直接lineTo，这样线条转折处会带有弧度
                    Point leastP = shape[i - 1];
                    path.moveTo(leastP.x, leastP.y);
                    path.lineTo(p.x, p.y);
                }
            }
            if(linePart < shape.length && linePart > 0){
                Point endP = shape[linePart];
                Point startP = shape[linePart - 1];
                float xProgress = (endP.x - startP.x) * lineProgress;
                float yProgress = (endP.y - startP.y) * lineProgress;
                if(linePart == 7 || linePart == 9)
                    path.moveTo(endP.x, endP.y);
                else
                    path.lineTo(startP.x + xProgress, startP.y + yProgress);
            }
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 绘制加载过程
     * @param canvas
     * @param color
     * @param outProgress
     */
    private void drawOutProgress(Canvas canvas, int color, int floor, float outProgress){
        if(progressShape != null){
            Paint progressPaint = new Paint(mPaint);
            int progressLum = floor%2 == 0 ? (int) (60 - outProgress * 120) : (int) (60 - (1-outProgress) * 120);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(new float[] {
                    1,0,0,0,progressLum,
                    0,1,0,0,progressLum,
                    0,0,1,0,progressLum,
                    0,0,0,1,0,
            });
            progressPaint.setColor(mParent.getResources().getColor(color));
            progressPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            progressPaint.setStrokeWidth(LOGO_LINEWIDTH_SMALL);
            progressPaint.setStyle(Paint.Style.FILL);
            Path path = new Path();
            float pre_max = 1f/(float) (progressShape.length);
            float progress = outProgress/pre_max;
            float lineProgress = progress%1f;
            int linePart = (int) Math.ceil(progress);

            for(int i = 0; i< Math.min(progressShape.length, linePart); i++){
                Point p = progressShape[i];
                if(i == 0)
                    path.moveTo(p.x, p.y);
                else{
                    //先移动后连线而不是直接lineTo，这样线条转折处会带有弧度
                    Point leastP = progressShape[i - 1];
                    path.moveTo(leastP.x, leastP.y);
                    path.lineTo(p.x, p.y);
                }
            }
            if(linePart < progressShape.length + 1 && linePart > 0){
                Point endP;
                Point startP;
                if(linePart == progressShape.length){
                    endP = progressShape[0];
                    startP = progressShape[linePart - 1];
                }else{
                    endP = progressShape[linePart];
                    startP = progressShape[linePart - 1];
                }
                float xProgress = (endP.x - startP.x) * lineProgress;
                float yProgress = (endP.y - startP.y) * lineProgress;
                path.lineTo(startP.x + xProgress, startP.y + yProgress);
                canvas.drawCircle(startP.x + xProgress, startP.y + yProgress, lineWidth/3, progressPaint);
            }
//            canvas.drawPath(path, mPaint);
            ViewCompat.postInvalidateOnAnimation(mParent);
        }
    }

    private void drawLines(Canvas canvas, int color){
        mPaint.setColor(mParent.getResources().getColor(color));
        mPaint.setStrokeWidth(lineWidth);
        Path path = new Path();
        if(shape != null){
            for(int i=0;i<shape.length;i++){
                Point p = shape[i];
                if(i == 0 || i == 7 || i == 9)
                    path.moveTo(p.x, p.y);
                else{
                    //先移动后连线而不是直接lineTo，这样线条转折处会带有弧度
                    Point leastP = shape[i - 1];
                    path.moveTo(leastP.x, leastP.y);
                    path.lineTo(p.x, p.y);
                }
            }
        }
        canvas.drawPath(path, mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        final int width = mBounds.width();
        final int height = mBounds.height();
        final int edge = (logoHeight/2);
        final int bevel = (edge/2);
        final int subEdge = ((edge + bevel)/2);
        final int subBevel = (subEdge/2);
        shape = new Point[]{
        new Point(width/2 + logoWidth/2, LOGO_MARGINTOP + bevel),
        new Point(width/2, LOGO_MARGINTOP),
        new Point(width/2 - logoWidth/2, LOGO_MARGINTOP + bevel),
        new Point(width/2 - logoWidth/2, LOGO_MARGINTOP + logoHeight - bevel),
        new Point(width/2, LOGO_MARGINTOP + logoHeight),
        new Point(width/2 + logoWidth/2, LOGO_MARGINTOP + logoHeight - bevel),
        new Point(width/2 + logoWidth/2, LOGO_MARGINTOP + bevel),
        new Point(width/2 + logoWidth/2 - 5, LOGO_MARGINTOP + bevel + 5),
        new Point(width/2, LOGO_MARGINTOP + logoHeight/2),
        new Point(width/2, LOGO_MARGINTOP + logoHeight/6 + lineWidth/2 + 2),
        new Point(width/2 - logoWidth/4, LOGO_MARGINTOP + bevel/2 + subBevel + lineWidth/2),
        new Point(width/2 - logoWidth/4, LOGO_MARGINTOP + logoHeight - bevel/2 - subBevel - lineWidth/2),
        new Point(width/2, LOGO_MARGINTOP + logoHeight - logoHeight/6 - lineWidth/2 - 2),
        new Point(width/2 + logoWidth/4, LOGO_MARGINTOP + logoHeight - bevel/2 - subBevel - lineWidth/2)};

        progressShape = Arrays.copyOfRange(shape, 0, 6);
    }

    /**
     * 亮度调节  亮度范围  -127--126
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setLum(int lum){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[] {
                1,0,0,0,lum,
                0,1,0,0,lum,
                0,0,1,0,lum,
                0,0,0,1,0,
        });
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        ViewCompat.postInvalidateOnAnimation(mParent);
    }

    @Override
    public void setColorScheme(int... colors) {
        if(colors != null && colors.length > 0)
            lineColor = colors[0];
    }

    @Override
    public void stop() {
        super.stop();
        if(runningListener != null)
            runningListener.onRealFinish();
    }
}
