package com.leconssoft.common.widgt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;


/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/12/26 17:13
 * @Description
 */
@SuppressLint("AppCompatCustomView")
public class RedTextView extends TextView {
    String str;
    public RedTextView(Context context) {
        super(context);
    }

    public RedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        str =this.getText().toString().trim();

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(Html.fromHtml("<font color=\"#ff0000\">*</font>"+ text), type);
    }
}
