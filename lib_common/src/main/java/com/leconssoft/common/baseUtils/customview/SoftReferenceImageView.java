package com.leconssoft.common.baseUtils.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.leconssoft.common.baseUtils.FileUtils;
import com.leconssoft.common.baseUtils.ImageUtil;
import com.leconssoft.common.baseUtils.MD5;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.LinkedHashMap;

import androidx.annotation.DrawableRes;

/**
 * 异步加载图片
 * <p>
 * 改用第三方控件ImageLoader加载图片（2015-9-1 by crb）
 */
public class SoftReferenceImageView extends ImageView {

    /**
     * *****************************默认设置 start
     ***********************************/

    // private static final int DEFAULT_IMG = R.drawable.home_bg_icon; // 设置默认图片
    private boolean IS_SAVE_LOCAL = false; // 是否把图片保存本地文件，默认false
//	private static final String Cache_Dir = "picture"; // SD卡 目录名称
    // ，默认background

    /**
     * *****************************默认设置 end
     **********************************/

    private static final String TAG = SoftReferenceImageView.class
            .getSimpleName();
    private Context context;
    private View view;
    private ScaleType scaleType;
    DisplayImageOptions options;
    private boolean raund = true;

    /**
     * Default image shown while loading or on url not found
     */
    private Integer mDefaultImage;

    /**
     * 内存图片软引用缓冲
     */
    public static LinkedHashMap<String, SoftReference<Bitmap>> imageCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
            20);

    public SoftReferenceImageView(Context context) {
        super(context);
        this.context = context;
    }

    public SoftReferenceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SoftReferenceImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * Sets default local image shown when remote one is unavailable
     *
     * @param resid
     */
    public void setDefaultImage(Integer resid) {

        mDefaultImage = resid;
        if (raund) {
            setImageResource(resid);
            options =
                    new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true)
                            .displayer(new SimpleBitmapDisplayer())
                            .build();
        } else {
            setImageBitmap(ImageUtil.getRuandPic(resid, context, 20));
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(false)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }

    }


    /**
     * 设置自定义圆角的图片
     *
     * @param resid
     * @param rond
     */
    public void setDefaultImage(Integer resid, int rond) {

        mDefaultImage = resid;
        setImageBitmap(ImageUtil.getRuandPic(resid, context, rond));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .displayer(new RoundedBitmapDisplayer(rond))
                .build();

    }

    /**
     * 设置圆角 true 不设置
     *
     * @param rund
     */
    public void setRaund(boolean rund) {
        raund = rund;
    }

    /**
     * 加载默认图片
     */
    public void loadDefaultImage() {
        if (mDefaultImage != null) {
            setImageResource(mDefaultImage);
        }
    }

    /**
     * 设置正在加载时的View，图片加载结束后隐藏
     *
     * @param view 加载时的View
     */
    public void setLoadingView(View view) {
        this.view = view;
        this.view.setVisibility(View.VISIBLE);
    }

    /**
     * 入口2 设置图片URL
     *
     * @param imgUrl
     */
    public void setImageUrlAndSaveLocal(String imgUrl,
                                        boolean isSaveNative, ScaleType scaleType) {
        IS_SAVE_LOCAL = isSaveNative;
        this.scaleType = scaleType;
        if (TextUtils.isEmpty(imgUrl)) {
            loadDefaultImage();
            return;
        }
        String imagePath = null;
        if (IS_SAVE_LOCAL) {

            String md5 = SoftReferenceImageView.md5(imgUrl);
            String cache_dir = FileUtils.getExternalCacheDir(context);

            File imgFile = new File(cache_dir + "/CACHE_IMG/");
            if (!imgFile.exists()) {
                imgFile.mkdirs();
            }

            imagePath = cache_dir + "/CACHE_IMG/" + md5;

        }

        Bitmap mBitmap = LoadCacheImage(imgUrl, imagePath);

        if (mBitmap != null) {
            this.setImageBitmap(mBitmap);
            if (null != this.scaleType) {
                SoftReferenceImageView.this.setScaleType(this.scaleType);
            }

            if (this.view != null) {
                this.view.setVisibility(View.GONE);
            }
        } else {

            try {

                if (imgUrl.startsWith("http")) {

                } else if (!imgUrl.startsWith("content://")) {

                    imgUrl = "file:///" + imgUrl;
                }
                Log.d("showImage", "real url is " + imgUrl);
                if(!hasResource)
                    ImageLoader.getInstance().displayImage(imgUrl, this, options);
                if (null != SoftReferenceImageView.this.scaleType) {
                    SoftReferenceImageView.this
                            .setScaleType(SoftReferenceImageView.this.scaleType);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    boolean hasResource;
    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        if(mDefaultImage != null)
            hasResource = resId != mDefaultImage;
        else
            hasResource = true;
    }

    /**
     * 根据图片的URL ，获取Bitmap
     *
     * @param mActivity
     * @param picUrl    //图片url
     * @param dwRatio   //显示的图片view相对屏幕宽度的比例 , 此值越大，图片的质量越低、内存问题越少。
     * @return
     * @throws Exception
     */
    public static Bitmap loadBitmapByUrl(Activity mActivity, String picUrl,
                                         float dwRatio, int reqWidth, int reqHeight) throws Exception {

        URL tempUrl = new URL(picUrl);
        InputStream inputStream = (InputStream) tempUrl.getContent();
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        return bmp;
    }

    /**
     * 加载本地缓存图片
     *
     * @param imgUrl
     * @param imagePath
     * @return
     */
    private Bitmap LoadCacheImage(final String imgUrl, final String imagePath) {
        // 在软链接缓存中，则返回Bitmap对象
        if (imageCache.containsKey(imgUrl)) {
            SoftReference<Bitmap> reference = imageCache.get(imgUrl);
            Bitmap bitmap = reference.get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        if (IS_SAVE_LOCAL) {
            return getImageFromLocal(imagePath);// 从本地加载
        }
        return null;
    }

    /**
     * 从SD卡加载图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getImageFromLocal(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                file.setLastModified(System.currentTimeMillis());
                return bitmap;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 保存图片到SD卡
     *
     * @param imagePath
     * @param bm
     */
    public static void saveImageToLocal(String imagePath, Bitmap bm) {

        if (bm == null || imagePath == null || "".equals(imagePath)) {
            return;
        }

        File f = new File(imagePath);
        if (f.exists()) {
            return;
        } else {
            try {
                File parentFile = f.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                f.createNewFile();
                FileOutputStream fos;
                fos = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                f.delete();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                f.delete();
            }
        }
    }

    /**
     * 获得
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @SuppressLint("NewApi")
    public static String getExternalCacheDir(Context context) {
        // android 2.2 以后才支持的特性
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir().getPath() + File.separator
                    ;
        }
        // 2.2以前我们需要自己构造
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return Environment.getExternalStorageDirectory().getPath() + cacheDir;
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * MD5
     *
     * @param paramString
     * @return
     */
    private static String md5(String paramString) {
        return MD5.crypt(paramString);
    }

    /**
     * 检测SD卡是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean sdCardIsAvailable() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED))
            return false;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("ABC onMeasure", "widthMeasureSpec    " + getMeasuredWidth() + "heightMeasureSpec  " + getMeasuredHeight());
    }

}