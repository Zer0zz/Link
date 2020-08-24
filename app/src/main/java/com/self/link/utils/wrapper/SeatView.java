package com.self.link.utils.wrapper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.self.link.R;

import java.util.ArrayList;

/**
 * 选座控件
 */
public class SeatView extends View {
    private final String TAG = "SeatView";
    private final boolean DBG = true;
    Paint paint = new Paint();
    Matrix matrix = new Matrix();
    Matrix tempMatrix = new Matrix();
    //座位水平间距
    int spacing;
    //座位垂直间距
    int verSpacing;
    //行号宽度
    int numberWidth;
    //行数
    int row;
    //列数
    int column;

    Bitmap mSeatBitmap;

    //可选座位的图片
    Bitmap seatBitmap;
    //选中时座位的图片
    Bitmap checkedSeatBitmap;
    //不可选座位的bitmap图片
    Bitmap cantSeatBitmap;

    int lastX;
    int lastY;
    int screenHeight;

    //标识是否正在缩放
    boolean isScaling;

    //枢轴点
    float scaleX, scaleY;
    //是否是第一次缩放
    boolean firstScale = true;
    boolean isOnClick;
    private int downX, downY;
    private boolean pointer;
    //用于存储已经选在好的座位
    public ArrayList<Point> list;
    /**
     * 座位图片的宽度
     */
    private int seatWidth = 48;
    /**
     * 座位图片的高度
     */
    private int seatHeight = 42;

    private float zoom;

    /**
     * 座位图片的初始缩放比例
     */
    float xScalel = 1, yScalel = 1;
    /**
     * 座位的分布情况
     */
    private int[][] mData;


    public SeatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("构造---- SeatView", "SeatView");
        init(context, attrs);
    }


    boolean isfirstMeasure = true;
    /**
     * 测量控件的宽高
     */
    int viewW;
    int viewH;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewW = MeasureSpec.getSize(widthMeasureSpec);
        viewH = MeasureSpec.getSize(heightMeasureSpec);

        //计算座位的宽高
        seatWidth = (viewW - numberWidth - spacing) / row - spacing;
        seatHeight = (viewH - screenHeight - verSpacing) / column - verSpacing;

        xScalel = (float) (seatWidth * 1.0 / seatBitmap.getWidth());
        yScalel = (float) (seatHeight * 1.0 / seatBitmap.getHeight());
        if (isfirstMeasure) {
            isfirstMeasure = false;
            matrix.postTranslate(numberWidth, screenHeight);
        }
    }


    private void init(Context context, AttributeSet attrs) {
        numberWidth = (int) dip2px(25);
        screenHeight = (int) dip2px(25);
        paint.setColor(Color.RED);
        list = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeatView);
        if (typedArray == null)
            return;
        int unselectIcon = typedArray.getResourceId(R.styleable.SeatView_unselectIcon, R.mipmap.seat_unselect_icon);
        seatBitmap = BitmapFactory.decodeResource(getResources(), unselectIcon);
        Log.e("SeatView", "seatBitmap.getHeight():" + seatBitmap.getHeight() + "-- seatBitmap.getWidth():" + seatBitmap.getWidth());
        int myselectIcon = typedArray.getResourceId(R.styleable.SeatView_mySelectIcon, R.mipmap.seat_myselect_icon);
        checkedSeatBitmap = BitmapFactory.decodeResource(getResources(), myselectIcon);
        int selectedIcon = typedArray.getResourceId(R.styleable.SeatView_selectedIcon, R.mipmap.seat_selected_icon);
        cantSeatBitmap = BitmapFactory.decodeResource(getResources(), selectedIcon);

        column = typedArray.getInteger(R.styleable.SeatView_columns, 13);
        row = typedArray.getInteger(R.styleable.SeatView_rows, 13);
        Log.e("SeatView", "column:" + column + "-- row:" + row);
        spacing = (int) typedArray.getDimension(R.styleable.SeatView_horspaceing, 5);
        verSpacing = (int) typedArray.getDimension(R.styleable.SeatView_verspaceing, 10);
        Log.e("SeatView", "spacing:" + spacing + "-- verSpacing:" + verSpacing);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (row <= 0 || column <= 0) {
            return;
        }
        drawSeat(canvas);
        super.onDraw(canvas);
    }

    private long mdoubleClickTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        scaleGuestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();
        if (pointerCount > 1) {
            //多手指操作
            pointer = true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointer = false;
                downX = x;
                downY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                autoScale();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && !isOnClick) {
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !pointer) {
                        int dx = x - lastX;
                        int dy = y - lastY;
                        matrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }
                lastX = x;
                lastY = y;
                isOnClick = false;
                break;
        }

        return true;
    }

    private void drawSeat(Canvas canvas) {
        zoom = getMatrixScaleX();
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;
        for (int i = 0; i < row; i++) {
            float top = i * seatBitmap.getHeight() * yScalel * scaleY + i * verSpacing * scaleY + translateY;
            float bottom = top + seatBitmap.getHeight() * yScalel * scaleY;

            for (int j = -1; j < column; j++) {
                if (j == -1) {
                    drawSerialNumber(canvas, i, j, top, translateX - numberWidth * scaleY);
                    continue;
                }
                float left = j * seatBitmap.getWidth() * xScalel * scaleX + j * spacing * scaleX + translateX;
                float right = left + seatBitmap.getWidth() * xScalel * scaleX;
                tempMatrix.setTranslate(left, top);
                tempMatrix.postScale(xScalel, yScalel, left, top);
                tempMatrix.postScale(scaleX, scaleY, left, top);
                if (isHave(i, j)) {
                    canvas.drawBitmap(checkedSeatBitmap, tempMatrix, paint);
//                    drawText(canvas, i, j, top, left);
                } else {
                    mSeatBitmap = seatBitmap;
                    if (mData != null) {
                        if (mData[i][j] == 1) { //未选中 可以选择的
                            mSeatBitmap = seatBitmap;
                        } else if (mData[i][j] == 2) {// 空位置 不画
                            mSeatBitmap = null;
                        } else if (mData[i][j] == 3) { //已经被选中
                            mSeatBitmap = cantSeatBitmap;
                        } else if (mData[i][j] == 4) {//我的
                            mSeatBitmap = checkedSeatBitmap;
                        }
                    }
                    if (mSeatBitmap != null) {
                        canvas.drawBitmap(mSeatBitmap, tempMatrix, paint);
                    }

                }
            }
        }
    }

    private void autoScale() {
        if (getMatrixScaleX() > 2.2) {
            zoomAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < 0.98) {
            zoomAnimate(getMatrixScaleX(), 1.0f);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        seatBitmap.recycle();
        cantSeatBitmap.recycle();
        checkedSeatBitmap.recycle();
    }

    private void zoomAnimate(float cur, float tar) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
//        ZoomAnimation zoomAnim = new ZoomAnimation();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                zoom = (Float) animation.getAnimatedValue();
                zoom(zoom);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }


    private void zoom(float zoom) {
        float z = zoom / getMatrixScaleX();
        matrix.postScale(z, z, scaleX, scaleY);
        invalidate();
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animation.AnimationListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }

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

    ScaleGestureDetector scaleGuestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            isScaling = true;
            float scaleFactor = detector.getScaleFactor();
            if (getMatrixScaleY() * scaleFactor > 3) {
                scaleFactor = 3 / getMatrixScaleY();
            }
            if (firstScale) {
                scaleX = detector.getCurrentSpanX();
                scaleY = detector.getCurrentSpanY();
                firstScale = false;
            }
            if (getMatrixScaleY() * scaleFactor < 0.5) {
                scaleFactor = 0.5f * getMatrixScaleY();
            }
            matrix.postScale(scaleFactor, scaleFactor, scaleX, scaleY);
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            firstScale = true;

        }


    });


    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    int tempX = (int) ((j * seatWidth + j * spacing) * getMatrixScaleX() + getTranslateX());
                    int maxTempX = (int) (tempX + seatWidth * getMatrixScaleX());
                    int tempY = (int) ((seatHeight * i + i * verSpacing) * getMatrixScaleY() + getTranslateY());
                    int maxTempY = (int) (tempY + seatHeight * getMatrixScaleY());
                    if (x >= tempX && x <= maxTempX && y >= tempY && y <= maxTempY) {
                        if (isHave(i, j)) {
                            remove(i, j);
                        } else {
                            if (isCanSelect(i, j)) {
                                list.clear();
                                list.add(new Point(i, j));
                            }
                        }
                    }
                }
            }
            float currentScaleY = getMatrixScaleY();
            if (currentScaleY < 1.7) {
                scaleX = x;
                scaleY = y;
//                zoomAnimate(currentScaleY, 1.9f);
            }
            invalidate();
            return true;
        }
    });

    private void remove(int x, int y) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (Point point : list) {
            if (point.x == x && point.y == y) {
                list.remove(point);
                return;
            }
        }
    }

    private float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseLine = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseLine;
    }

    private void drawSerialNumber(Canvas canvas, int row, int column, float top, float left) {
        String txt = (row + 1) + "";
        String txt1 = null;
        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setColor(Color.BLACK);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = this.seatHeight * getMatrixScaleX();
        float seatWidth = this.seatWidth * getMatrixScaleX();
        txtPaint.setTextSize(seatHeight / 2);
        //获取中间线
        float center = seatHeight / 2;
        float txtWidth = txtPaint.measureText(txt);
        float startX = left + seatWidth / 2 - txtWidth / 2;
        //只绘制一行文字
        if (txt1 == null) {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + seatHeight), txtPaint);
        } else {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + center), txtPaint);
            canvas.drawText(txt1, startX, getBaseLine(txtPaint, top + center, top + center + seatHeight / 2), txtPaint);
        }
        if (DBG) {
            Log.d("drawTest", "top" + top);

        }
    }

    private void drawText(Canvas canvas, int row, int column, float top, float left) {
        String txt = (row + 1) + "排";
        String txt1 = (column + 1) + "座";
        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = this.seatHeight * getMatrixScaleX();
        float seatWidth = this.seatWidth * getMatrixScaleX();
        txtPaint.setTextSize(seatHeight / 3);
        //获取中间线
        float center = seatHeight / 2;
        float txtWidth = txtPaint.measureText(txt);
        float startX = left + seatWidth / 2 - txtWidth / 2;
        //只绘制一行文字
        if (txt1 == null) {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + seatHeight), txtPaint);

        } else {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + center), txtPaint);
            canvas.drawText(txt1, startX, getBaseLine(txtPaint, top + center, top + center + seatHeight / 2), txtPaint);

        }
        if (DBG) {
            Log.d("drawTest", "top" + top);

        }
    }

    float[] m = new float[9];

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[4];
    }

    /**
     * 是否被选上
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isHave(int x, int y) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (Point point : list) {
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }

    public void setRect(int row, int column) {
        this.row = row;
        this.column = column;
        invalidate();
    }

    private float dip2px(float value) {
        return getResources().getDisplayMetrics().density * value;
    }

    public void setData(int[][] data) {
        this.mData = data;
        invalidate();
    }

    /**
     * 是否是不可选座位
     *
     * @param i
     * @param j
     * @return
     */
    private boolean isCanSelect(int i, int j) {
        // 1可选择的位置，2 空位置 不画 ，3已经被(别人)选中， 4 你的选择
        if (mData[i][j] == 1) {
            return true;
        } else if (mData[i][j] == 2 || mData[i][j] == 3 || mData[i][j] == 4) {
            return false;
        }
        return false;

    }

    public SeatPointKinks comfirmSelected() {

        return null;
    }

    public class SeatPointKinks {
        public int x;
        public int y;
        public int kink;//作为的选择情况 0 未选择 白色，1 我的选中 蓝色，2 不可选中的位置 红色

        public SeatPointKinks() {

        }

        public SeatPointKinks(int x, int y, int kink) {
            this.x = x;
            this.y = y;
            this.kink = kink;
        }
    }

    public int getSeatSerialNumber() {
        int seatId = 0;
        if (list != null && list.size() > 0) {
            int x = list.get(0).x;
            int y = list.get(0).y;
            seatId = x * 10 + (y+1);
        }
        return seatId;
    }

    public void clearMySelect(){
        list.clear();
    }
}
