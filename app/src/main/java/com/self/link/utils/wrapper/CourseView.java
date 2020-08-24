package com.self.link.utils.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.self.link.R;
import com.self.link.coach.body.ScheduleData;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * description：
 * author：zhang-chenglin on 2020/6/2 08:49
 */
public class CourseView extends View {

    private final static String TAG = "CourseView";
    Paint linePaint = new Paint();
    Paint paint = new Paint();
    Matrix matrix = new Matrix();
    Matrix tempMatrix = new Matrix();
    //控件的宽高
    int viewWidth, viewHeight;
    private OnCourseClickListener mOnCourseClickListener;
    //高宽比
    float aspectRatio = 0.7f;
    float tableWidth, tableHeight;
    String[] time = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
    //课程的背景图
//    Bitmap daidingBitmap;
    Bitmap selectedBitmap;
    Bitmap unselectBitmap;
    //课程状态  //1：已过预约时间 2：已被他人预约 3：已被自己预约 4：可以预约
    private final static int canSelect = 4;
//    private final static int UNSELECT = 1;
//    private final static int SELECT = 2;
//    private final static int DAIDING = 3;


    /**
     * 课程图片的初始缩放比例
     */
    float xScalel = 1, yScalel = 1;

    //
    int row = 12, column = 8;

    int dividerWidth;

    float[] m = new float[9];

    public CourseView(Context context) {
        super(context);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        linePaint.setColor(getResources().getColor(R.color.course_divider));
        linePaint.setStrokeWidth(dividerWidth);
        linePaint.setAntiAlias(true);

//        daidingBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.coach_course_daiding_bg);
        selectedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.coach_course_selected_bg);
        unselectBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.coach_course_unselect_bg);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CourseView);
        if (typedArray == null)
            return;
        aspectRatio = typedArray.getFloat(R.styleable.CourseView_aspectRatio, 0.7f);
        dividerWidth = (int) typedArray.getDimension(R.styleable.CourseView_dividerWidth, 1);
        column = typedArray.getInteger(R.styleable.CourseView_column, 8);
        row = typedArray.getInteger(R.styleable.CourseView_row, 12);
        typedArray.recycle();
    }

    boolean isfirstMeasure = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int width = measureWidth(widthMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        //计算课表表格的宽高
        tableWidth = (float) ((viewWidth * 1.0 - dividerWidth * (column - 1)) / column);
        tableHeight = (tableWidth / aspectRatio);
        if (isfirstMeasure) {
            viewHeight = (int) (tableWidth / aspectRatio) * row + (row - 1) * dividerWidth;
            isfirstMeasure = false;
        }
        setMeasuredDimension(viewWidth, viewHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e(TAG, "viewWidth:" + viewWidth + "---viewHeight:" + viewHeight);

        xScalel = (float) (tableWidth * 1.0 / selectedBitmap.getWidth());
        yScalel = (float) (tableHeight * 1.0 / selectedBitmap.getHeight());
    }


    //根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);

        int specSize = MeasureSpec.getSize(measureSpec);

        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {

        }
//        Log.i("这个控件的宽度----------", "specMode=" + specMode + " specSize=" + specSize);
        return specSize;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawTableLine(canvas);
        drawTimeColumn(canvas);
        if (mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                ScheduleData scheduleData = mDataList.get(i);
                drawCourses(canvas, scheduleData);
                drawText(canvas, scheduleData);
            }
        }
        super.onDraw(canvas);

    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (MotionEvent.ACTION_UP == e.getAction()) {
                Log.e("ACTION", "MotionEvent.ACTION_UP");
            }
            int x = (int) e.getX();
            int y = (int) e.getY();
            //根据所点的点  判断所在的课程位置
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {

                    int tempX = (int) ((j * tableWidth + j * dividerWidth) + getTranslateX());
                    int maxTempX = (int) (tempX + tableWidth);

                    int tempY = (int) ((tableHeight * i + i * dividerWidth) + getTranslateY());
                    int maxTempY = (int) (tempY + tableHeight);

                    if (x >= tempX && x <= maxTempX && y >= tempY && y <= maxTempY) {
                        if (isHave(i, j)) {//如果已选中就取消选中
                            remove(i, j);
//                            if (mOnCourseClickListener!=null){
//                                mOnCourseClickListener.onCourseClick(scheduleData);
//                            }
                        } else {
                            if (!(mDataList == null || mDataList.isEmpty())) {
                                for (ScheduleData scheduleData : mDataList) {//有课程
                                    //TODO 这里只考虑了两小时的课
                                    if (scheduleData.day == j
                                            && (scheduleData.timeStartPoint <= i && (scheduleData.timeStartPoint + scheduleData.timeLenth) > i)
//                                            && (scheduleData.timeStartPoint == i || (scheduleData.timeStartPoint + scheduleData.timeLenth - 1) == i)
                                    ) {
//                                        if (scheduleData.status == 1) {
//                                        mySelect.add(scheduleData);
                                        if (mOnCourseClickListener != null) {
                                            mOnCourseClickListener.onCourseClick(scheduleData);
                                        }
                                        //TODO 是否绘画
//                                        }

                                    }
                                }
                            }


                        }
                    }
                }
            }

            invalidate();
            return true;
        }
    });
    List<ScheduleData> mySelect;

    /**
     * 是否被选上
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isHave(int x, int y) {
        if (mySelect == null || mySelect.isEmpty()) {
            return false;
        }
        for (ScheduleData scheduleData : mySelect) {
            //TODO 这里只考虑了两小时的课
            if (scheduleData.day == x
                    && (scheduleData.timeStartPoint <= y && (scheduleData.timeStartPoint + scheduleData.timeLenth) > y)
//                    && (scheduleData.timeStartPoint == y || scheduleData.timeStartPoint + scheduleData.timeLenth - 1 == y)
            ) {
                return true;
            }
        }
        return false;
    }

    private void remove(int x, int y) {
        if (mySelect == null || mySelect.isEmpty()) {
            return;
        }
        for (ScheduleData scheduleData : mySelect) {
            //TODO 这里只考虑了两小时的课
            if (scheduleData.day == x
                    && (scheduleData.timeStartPoint <= y && (scheduleData.timeStartPoint + scheduleData.timeLenth) > y)
//                    && (scheduleData.timeStartPoint == y || scheduleData.timeStartPoint + scheduleData.timeLenth - 1 == y)
            ) {
                mySelect.remove(scheduleData);
                return;
            }
        }
    }

    /**
     * 是否是不可选课程
     *
     * @param i
     * @param j
     * @return
     */
    private boolean isCanSelect(int i, int j) {
        if (mDataList == null || mDataList.isEmpty()) {
            return false;
        }
        for (ScheduleData scheduleData : mySelect) {//有课程
            if (scheduleData.day == i && scheduleData.timeStartPoint == j) {
                if (scheduleData.status == 1) {
                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }


    private void drawCourses(Canvas canvas, ScheduleData scheduleData) {
        Bitmap bitmap;

        if (scheduleData.status == 4)
            bitmap = unselectBitmap;
        else bitmap = selectedBitmap;

        float left = scheduleData.day * tableWidth + scheduleData.day * dividerWidth;
        float top = scheduleData.timeStartPoint * tableHeight + scheduleData.timeStartPoint * dividerWidth;
        tempMatrix.setTranslate(left, top);
        tempMatrix.postScale(xScalel, yScalel * scheduleData.timeLenth, left, top);
        canvas.drawBitmap(bitmap, tempMatrix, paint);

    }

    private void drawText(Canvas canvas, ScheduleData scheduleData) {
        float left = scheduleData.day * tableWidth + scheduleData.day * dividerWidth;
        float top = scheduleData.timeStartPoint * tableHeight + scheduleData.timeStartPoint * dividerWidth;
        if (scheduleData.personalName == null) {
            return;
        }
        String txt = scheduleData.personalName;
        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        txtPaint.setTextSize(tableWidth / 5);
        //获取中间线
//        float center = tableHeight * scheduleData.timeLenth / 2;
        float txtWidth = txtPaint.measureText(txt);
        float startX = left + tableWidth / 2 - txtWidth / 2;
        //只绘制一行文字
//        if (txt1 == null) {
        canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + tableHeight * scheduleData.timeLenth), txtPaint);

//        } else {
//            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + center), txtPaint);
//            canvas.drawText(txt1, startX, getBaseLine(txtPaint, top + center, top + center + seatHeight / 2), txtPaint);
//
//        }

    }

    private float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseLine = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseLine;
    }

    /**
     * 绘画网格线
     *
     * @param canvas
     */
    private void drawTableLine(Canvas canvas) {
        for (int j = 1; j < column; j++) {
            float left = j * tableWidth + (j - 1) * dividerWidth;
            canvas.drawLine(left, 0, left, viewHeight, linePaint);
        }

        for (int i = 1; i < row; i++) {
            float top = i * tableHeight + (i - 1) * dividerWidth;
            canvas.drawLine(0, top, viewWidth, top, linePaint);
        }
    }

    //绘画时间列
    private void drawTimeColumn(Canvas canvas) {

        linePaint.setColor(getResources().getColor(R.color.course_divider));
        RectF rectF = new RectF(0, 0, tableWidth, viewHeight);
        canvas.drawRect(rectF, linePaint);


        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(30);

        for (int i = 0; i < row; i++) {
            String curTime = time[i];
            float txtWidth = textPaint.measureText(curTime);

            float startX = (float) (tableWidth - txtWidth * 1.1);
            float top = i * tableHeight + i * dividerWidth;
//            Log.e(TAG, "startX:" + startX + "---top:" + top);
            canvas.drawText(curTime, startX, getBaseLine(textPaint, top), textPaint);
        }

    }

    private float getBaseLine(Paint p, float top) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        float baseLine = (float) (top + (fontMetrics.bottom - fontMetrics.top) * 1.1);
        return baseLine;
    }

//    private float getMatrixScaleX() {
//        matrix.getValues(m);
//        return m[Matrix.MSCALE_X];
//    }

    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    List<ScheduleData> mDataList;

    public void setCoachScheduleData(List<ScheduleData> dataList) {
        mDataList = dataList;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unselectBitmap.recycle();
//        daidingBitmap.recycle();
        selectedBitmap.recycle();
    }

    //    private float getMatrixScaleY() {
//        matrix.getValues(m);
//        return m[4];
//    }


    public void setOnCourseClickListener(OnCourseClickListener listener) {
        mOnCourseClickListener = listener;
    }

    public interface OnCourseClickListener {
        void onCourseClick(ScheduleData scheduleData);
    }
}
