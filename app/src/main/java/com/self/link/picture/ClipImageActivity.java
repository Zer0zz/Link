package com.self.link.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.leconssoft.common.base.BaseHeadActivity;
import com.self.link.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class ClipImageActivity extends BaseHeadActivity {
    @BindView(R.id.clipImageLayout)
    ClipImageLayout clipImageLayout;
    private String path = "";
    private static final int CLIP = 1003;

    @Override
    public int layoutId() {
        return R.layout.activity_clip_image;
    }

    @Override
    protected void initUIData() {
        path = getIntent().getStringExtra("path");
//        EventBus.getDefault().register(this);
        setTvLeftMsg("头像选取");
        hideTvRight(View.VISIBLE);
        setTvRightMsg("确定");
        setIvLeftSrc(R.mipmap.back_icon);
        initView();

    }

    @Override
    public void setOnClickTvRight() {
        super.setOnClickTvRight();
        String _path = saveBitmapToSDCard(clipImageLayout.clip(), path);
        Intent intent = new Intent();
        intent.putExtra("result_path", _path);
        setResult(CLIP, intent);
        finish();
    }


    protected void initView() {
        // 有的系统返回的图片是旋转了，有的没有旋转，所以处理
        int degreee = readBitmapDegree(path);
        Bitmap bitmap = createBitmap(path);
        if (bitmap != null) {
            if (degreee == 0) {
                clipImageLayout.setImageBitmap(bitmap);
            } else {
                clipImageLayout.setImageBitmap(rotateBitmap(degreee, bitmap));
            }
        } else {
            finish();
        }
    }


    private Bitmap createBitmap(String path) {
//        FileInputStream fis = new FileInputStream("/sdcard/test.png");
//        Bitmap bitmap  = BitmapFactory.decodeStream(fis);

        if (path == null) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //不在内存中读取图片的宽高
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;

        opts.inSampleSize = width > 1080 ? (int) (width / 1080) : 1;//注意此处为了解决1080p手机拍摄图片过大所以做了一定压缩，否则bitmap会不显示

        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    // 读取图像的旋转度
    private int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return degree;
    }

    // 旋转图片
    private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     *                return 生成压缩图片后的图片路径
     */
    public static String saveMyBitmap(String bitName, Bitmap mBitmap) {

        File f = new File("/sdcard/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("在保存图片时出错：" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception e) {
            return "create_bitmap_error";
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/sdcard/" + bitName + ".png";
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imagename
     */
    public String saveBitmapToSDCard(Bitmap bitmap, String imagename) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = getFilesDir().getAbsolutePath() + "/img-head_" + timeStamp + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    private File createImageFile() {
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp;
//
//        File dir = new File(saveDir);
//        if (!dir.exists()) {
//            dir.mkdirs(); // 创建文件夹
//        }
//        File image = new File(saveDir + "/" + imageFileName + ".jpg");
//        // Save a file: path for use with ACTION_VIEW intents
//        return image;
//    }

}