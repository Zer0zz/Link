package com.self.link.utils.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;

import com.self.link.R;

import androidx.appcompat.widget.AppCompatEditText;


/**
 * description：可设置hint提示字体大小的EditText
 * author：zhangCl on 2018/12/10 10:49
 */
public class HintSizeEditText extends AppCompatEditText {

    private final static String TAG = HintSizeEditText.class.getSimpleName();

    private String text;
    private float textSize;


//    public HintSizeEditText(Context context) {
//        super(context);
//    }

    public HintSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
    }

    public HintSizeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean result = super.onTouchEvent(event);
//        if (event.getAction() == MotionEvent.ACTION_MOVE||event.getAction() == MotionEvent.ACTION_DOWN){
//            this.getParent().requestDisallowInterceptTouchEvent(true);
////            return false;
//        }else if (event.getAction() == MotionEvent.ACTION_UP){
//            this.getParent().requestDisallowInterceptTouchEvent(false);
//        }
//        return result;
//    }

    private void initParams(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HintSizeEditText);
        if (typedArray != null) {
            text = typedArray.getString(R.styleable.HintSizeEditText_hintText);
            textSize = typedArray.getDimension(R.styleable.HintSizeEditText_hintTextSize, 5);
            typedArray.recycle();
        }
    }

    @SuppressLint({"DrawAllocation", "ClickableViewAccessibility"})
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(text);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan((int) textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setHint(new SpannedString(ss));




    }


}
