package com.self.link.utils.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * description：
 * author：Administrator on 2020/5/24 21:36
 */
public class ScrollViewExtend extends ScrollView {
    GestureDetector gestureDetector;

    public ScrollViewExtend(Context context) {
        super(context);
    }
    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
        gestureDetector.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

}
