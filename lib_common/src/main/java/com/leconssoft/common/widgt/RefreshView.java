package com.leconssoft.common.widgt;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.leconssoft.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/11/16 15:36
 * @Description
 */
public class RefreshView extends View {
    /**
     * 第一步：设置画布大小
     * 第二步：设置画笔
     * 第三步：设置画布路径
     * 第四步：设置动画效果
     */

    private int width;//控件宽度
    private int height;//控件高度
    private Paint colorPaint;
    private Paint textPaint;

    //是否在显示
    private boolean isShowing=true;

    /**
     * 动画开始时间
     */
    protected long mStartTime;

    private int marginTop = 20;
    int linePart;
    Path path = new Path();
    /**
     * 绘制区域
     */
    protected Rect mBounds = new Rect();
    ValueAnimator animator;

    private List<Point> drawPath = new ArrayList<>();
    private static final int[] LAYOUT_ATTRS = new int[]{android.R.attr.enabled};

    private int progress;//进度

    public RefreshView(Context context, int width, int height) {
        this(context, null);
        this.width = width;
        this.height = height;
    }

    public RefreshView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
        colorPaint = new Paint();
        colorPaint.setColor(getResources().getColor(R.color.bg_blue));
        colorPaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(getResources().getColor(R.color.x_red));
        textPaint.setStrokeWidth(1f);
    }


    /**
     * 测量：onMeasure()决定View的大小；
     * 布局：onLayout()决定View在ViewGroup中的位置；
     * 绘制：onDraw()决定绘制这个View。
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setBounds(left, top, right, bottom);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;
    }


    /**
     * 画个两个圆
     */
    private void drawCircle(Canvas canvas) {
        Path bigRound = new Path();
        bigRound.addCircle(mBounds.width() / 2, height / 2 + marginTop, 70, Path.Direction.CW);
        bigRound.addCircle(mBounds.width() / 2, height / 2 + marginTop, 60, Path.Direction.CW);
        canvas.drawPath(bigRound, colorPaint);
    }

    /**
     * 绘制图形
     *
     * @param canvas
     */
    private void drawShape(Canvas canvas, int progress) {
        if (drawPath.size() > 0) {
            Point point = drawPath.get(progress);
            if(progress==0){
                path.reset();
            }
            if (progress == 0 || progress == 4) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
            canvas.drawPath(path, textPaint);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (isShowing) {
            drawCircle(canvas);
            drawShape(canvas,linePart);
        } else {
            drawCircle(canvas);
        }
    }

    /**
     * (width/3,0)         (width/3+60,0)
     * 0------------------------
     * ***************
     * *             *
     * *             *
     * *             *
     * ***************
     * (width/3,60)    (width/3+60,60)
     * heigh-------------------------
     *
     * @param canvas
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        drawPath.clear();
        drawPath.add(new Point(mBounds.width() / 2, height / 2 + marginTop - 50));
        drawPath.add(new Point(mBounds.width() / 2 - 50, height / 2 + marginTop + 30));
        drawPath.add(new Point(mBounds.width() / 2 + 50, height / 2 + marginTop + 30));
        drawPath.add(new Point(mBounds.width() / 2, height / 2 + marginTop - 50));
        drawPath.add(new Point(mBounds.width() / 2, height / 2 + marginTop + 50));
        drawPath.add(new Point(mBounds.width() / 2 + 50, height / 2 + marginTop - 30));
        drawPath.add(new Point(mBounds.width() / 2 - 50, height / 2 + marginTop - 30));
        drawPath.add(new Point(mBounds.width() / 2, height / 2 + marginTop + 50));
        animator = ValueAnimator.ofInt(0, drawPath.size());
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                linePart = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        drawCircle(canvas);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        mStartTime = 0;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    //动画开始
    public void start() {
        if (isShowing) {
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            animator.start();
            this.postInvalidate();
        }
    }

    public void stop() {
        if (isShowing) {
            isShowing = false;
             animator.cancel();
        }
    }

}
