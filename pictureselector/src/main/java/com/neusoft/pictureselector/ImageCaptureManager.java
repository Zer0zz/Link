package com.neusoft.pictureselector;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageCaptureManager {

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;

    private String mCurrentPhotoPath;
    private Context mContext;
    public static String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/scans";//相关文档存放路径
    public static String STORAGE_PICTURE = STORAGE_PATH + "/pic";// 相关图片存放路径
    //系统数据库存放图片的路径
    private static final Uri STORAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    public ImageCaptureManager(Context mContext) {
        this.mContext = mContext;
    }

    private File createImageFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        String saveDir = STORAGE_PICTURE.toString();
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建文件夹
        }
        File image = new File(saveDir + "/" + imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public Intent dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = createImageFile();
            // Continue only if the File was successfully created
            Uri uri = null;
            if (photoFile != null) {
                //TODO  在sdk版本<24时 Uri uri = Uri.fromFile(photoFile);
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    uri = FileProvider.getUriForFile(mContext, "com.self.link.provider", photoFile);
                } else {
                    uri = Uri.fromFile(photoFile);
                }
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        }
        return takePictureIntent;
    }


    /**
     * 添加图片到系统数据库
     */
    public static Uri addImage(final ContentResolver cr, final String path) {
        File file = new File(path);
        String name = file.getName();
        int i = name.lastIndexOf(".");
        String title = name.substring(0, i);// 文件名称
        String filename = title + name.substring(i);
        int[] degree = new int[1];
        return addImage(cr, title,
                System.currentTimeMillis(), null, file.getParent(),
                filename, degree);
    }

    /**
     * 添加图片到系统数据库
     */
    private static Uri addImage(ContentResolver cr, String title,
                                long dateTaken, Location location, String directory,
                                String filename, int[] degree) {
        // Read back the compressed file size.
        File file = new File(directory, filename);
        long size = file.length();
        ContentValues values = new ContentValues(9);
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.ORIENTATION, degree[0]);
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.SIZE, size);

        if (location != null) {
            values.put(MediaStore.Images.Media.LATITUDE, location.getLatitude());
            values.put(MediaStore.Images.Media.LONGITUDE, location.getLongitude());
        }

        return cr.insert(STORAGE_URI, values);
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
//        addImage(mContext.getContentResolver(),mCurrentPhotoPath);
        mContext.sendBroadcast(mediaScanIntent);
    }


    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }

}
