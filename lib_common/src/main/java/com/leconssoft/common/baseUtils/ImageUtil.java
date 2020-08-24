package com.leconssoft.common.baseUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {

	    //放大缩小图片
	    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        Matrix matrix = new Matrix();
	        float scaleWidht = ((float)w / width);
	        float scaleHeight = ((float)h / height);
	        matrix.postScale(scaleWidht, scaleHeight);
	        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	        return newbmp;
	    }
	    //将Drawable转化为Bitmap
	     public static Bitmap drawableToBitmap(Drawable drawable){
	            int width = drawable.getIntrinsicWidth();
	            int height = drawable.getIntrinsicHeight();
	            Bitmap bitmap = Bitmap.createBitmap(width, height,
	                    drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
	                            : Config.RGB_565);
	            Canvas canvas = new Canvas(bitmap);
	            drawable.setBounds(0,0,width,height);
	            drawable.draw(canvas);
	            return bitmap;

	        }

	     //获得圆角图片的方法
	    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx){

	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
	                .getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);

	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        final RectF rectF = new RectF(rect);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	        return output;
	    }
	    //获得带倒影的图片方法
	    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap){
	        final int reflectionGap = 4;
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();

	        Matrix matrix = new Matrix();
	        matrix.preScale(1, -1);

	        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,
	                0, height/2, width, height/2, matrix, false);

	        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);

	        Canvas canvas = new Canvas(bitmapWithReflection);
	        canvas.drawBitmap(bitmap, 0, 0, null);
	        Paint deafalutPaint = new Paint();
	        canvas.drawRect(0, height,width,height + reflectionGap,
	                deafalutPaint);

	        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

	        Paint paint = new Paint();
	        LinearGradient shader = new LinearGradient(0,
	                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
	                + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
	        paint.setShader(shader);
	        // Set the Transfer mode to be porter duff and destination in
	        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	        // Draw a rectangle using the paint with our linear gradient
	        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
	                + reflectionGap, paint);

	        return bitmapWithReflection;
	    }



	    /**
	     * 获取圆角图片
	     * @param reid
	     * @param context
	     * @param roundPx
	     * @return
	     */
	    public static Bitmap getRuandPic(int reid, Context context, float roundPx){
	    	BitmapDrawable drawable=(BitmapDrawable) context.getResources().getDrawable(reid);
			return getRoundedCornerBitmap(drawable.getBitmap(), roundPx);
	    }

	/**
	 * 存放身份证拍摄图片
	 */
	public static void saveIdCardIamge(Bitmap bitmap){
		final File dirFile = new File(Constants.STORAGE_PICTURE);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		String fileName="idCard.jpg";
		File file=new File(Constants.STORAGE_PICTURE,fileName);
        OutputStream outputStream = null;
        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取身份证截图
     * @return
     */
    public static Bitmap showIdCardBitmap(){
        final File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName=Constants.STORAGE_PICTURE+"/idCard.jpg";
       return BitmapFactory.decodeFile(fileName);
    }
}
