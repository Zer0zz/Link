package com.self.link.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.android.material.tabs.TabLayout;
import com.self.link.LinkApp;
import com.self.link.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description：通用工具类
 * author：zhangCl on 2018/10/24 15:50
 */
public class CommUtils {

    /**
     * 身份证号码的正则校验
     *
     * @param text 身份证号
     * @return 格式正确与否
     */
    public static boolean checkIdNum(String text) {
        Pattern patternSfzhm1 = Pattern
                .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        Pattern patternSfzhm2 = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher matcherSfzhm1 = patternSfzhm1.matcher(text);
        Matcher matcherSfzhm2 = patternSfzhm2.matcher(text);
        if (!matcherSfzhm1.matches() && !matcherSfzhm2.matches())
            return false;
        else return true;
    }

    /**
     * 电话号码的正则表达式
     *
     * @param phoneNumber 检验的电话号码
     * @return 格式是否正确
     */
    public static boolean isPhoneNum(String phoneNumber) {
        //电话号码的正则表达式
        //https://blog.csdn.net/DaBingShui/article/details/80498622
        String regExp = "(^((13[0-9])|(14[5-8])|(15([0-3]|[5-9]))|(16[6])|(17[0|4|6|7|8])|" +
                "(18[0-9])|(19[8-9]))\\d{8}$)|(^((170[0|5])|(174[0|1]))\\d{7}$)|(^(14[1|4])\\d{10}$)";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }



    /**
     * 邮箱的正则表达式
     *
     * @param emailCode 检验的邮箱地址
     * @return 格式是否正确
     */
    public static boolean checkEmailCode(String emailCode) {
        //^(\w+((-\w+)|(\.\w+))*)\+\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$
        String regExp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(emailCode);
        return m.matches();
    }


    /**
     * 对手机验证码格式进行判断
     *
     * @param verifyKey 输入的手机验证码
     * @return 是否正确格式
     */
    public static boolean checkVerifyKeyFormat(String verifyKey) {
        if (verifyKey.trim().length()!= 6){
            return false;
        }
        Pattern pattern = Pattern.compile("\\d{6}");
        boolean matches = pattern.matcher(verifyKey).matches();
        return matches;
    }

    public static void setClickBackEffect(View view){
        view.setOnTouchListener((v, event) -> {
            event.getAction();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getBackground().setAlpha(160);
                    break;
                case MotionEvent.ACTION_UP:
                    v.getBackground().setAlpha(255);
                    break;
            }
            return false;
        });
    }

    //设置EditText获取焦点提示错误信息
    public static void setRequestFocusWarming(EditText editText, String msg) {
        Drawable drawable = LinkApp.getIns().getResources().getDrawable(R.drawable.error);
        drawable.setBounds(0, 0, 40, 40);
        editText.setError(msg, drawable);
        editText.requestFocus();
    }

    //设置EditText无焦点提示错误信息
    public static void setNoFocusWarming(EditText editText, String msg) {
        Drawable drawable = LinkApp.getIns().getResources().getDrawable(R.drawable.error);
        drawable.setBounds(0, 0, 40, 40);
        editText.setError(msg, drawable);
    }
    public static Bitmap drawTextToBitmap(Context mContext, Bitmap bitmap, String mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            // Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            paint.setDither(true);  //获取跟清晰的图像采样
            paint.setFilterBitmap(true);//过滤一些
            paint.setColor(Color.RED);
            paint.setTextSize((int) (50));
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);//阴影制作半径，x偏移量，y偏移量，阴影颜色
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int y = (bitmap.getHeight() + bounds.height()) / 2;
//            int y = bitmap.getHeight()/2+bounds.height()/2;
            int x = (bitmap.getWidth() - bounds.width()) / 2;;
//            Toast.makeText(mContext, "x" + bitmap.getHeight() + "y" + y + "y*sace" + y * scale + "sace" + scale, Toast.LENGTH_LONG).show();
            Log.v("===位置", "x" + bitmap.getHeight() + "y" + y + "y*sace" + y * scale + "sace" + scale);
//            canvas.drawText(mText, x * scale, y * scale, paint);
            canvas.drawText(mText, x , y , paint);
//             canvas.drawText(mText, x * scale, 210, paint);
//             canvas.drawText(str3, x * scale,210 + 25 , paint);
            Log.v("===合成图片", "====ok" + mText);
            return bitmap;
        } catch (Exception e) {
            //  TODO: handle exception
            return null;
        }

    }
    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //把bitmap转换成String
    public static String bitmapToBase64(Bitmap bm ) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

}