package com.leconssoft.common.baseUtils.customview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leconssoft.common.R;
import com.leconssoft.common.base.BaseHeadActivity;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.baseUtils.Constants;
import com.leconssoft.common.baseUtils.UIHelper;
import com.leconssoft.common.baseUtils.csustomdialog.CommonAlertDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 小图看大图
 *
 * @author chenrb
 */
public class ChangePictureActivity extends BaseHeadActivity implements OnClickListener {

    private ImageView btn_back[], btn_zoomin[], btn_zoomout[], btn_rotate[];
    private Bitmap bmp;

    private LinearLayout[] ll_viewArea;
    private LinearLayout.LayoutParams parm;
    private ViewArea viewArea;

    private int screenW, screenH;// 屏幕宽度

    private String file_path;// 文件本地路径或者网络url

    private Context context;
    private Intent intent;
    private ViewPager mViewPager;

    private TextView tv_title;
    private ImageView ivLeft;
    private RelativeLayout layoutHead;

    private List<View> listViews; // Tab页面列表
    private List<String> pictureUrlLists = new ArrayList<String>();
    private int nowPicNumber = 0;//当前图片id


    private CommonAlertDialog.onMenuClickListener onUpDownClickListener = new CommonAlertDialog.onMenuClickListener() {

        @Override
        public void onMenuClick(int index) {
            if (1 == index) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//							URL url;
//							try {
//							url = new URL(file_path);
//							InputStream is = (InputStream) url.getContent();
//							Bitmap bitmap = BitmapFactory.decodeStream(is);
//							saveFile(bitmap, System.currentTimeMillis()+"");
//							is.close();
//							} catch (Exception e) {
//								// TODO: handle exception
//							}
//							saveImageFile(file_path);
                        downLoadImage(file_path);
                    }
                }).start();
            }
        }

    };


    public void downLoadImage(String imagePateh) {
        final File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(Constants.STORAGE_PICTURE, fileName);
//        HttpUtils util = new HttpUtils();
//        util.download(imagePateh, file.getAbsolutePath(), true, true, new RequestCallBack<File>() {
//            /**
//             * 重载方法
//             */
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<File> arg0) {
//                UIHelper.ToastMessage(ChangePictureActivity.this, "下载成功，请到SDCard下lecons文件夹查看");
//
//            }
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1) {
//
//                UIHelper.ToastMessage(ChangePictureActivity.this, "下载失败");
//            }
//        });

    }

    public void saveImageFile(String imagePath) {
        final File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        URL url;
        OutputStream output = null;
        try {
            url = new URL(imagePath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(Constants.STORAGE_PICTURE, fileName);
            InputStream input = conn.getInputStream();
            output = new FileOutputStream(file);
            //读取大文件
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = (input.read(buffer))) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    UIHelper.ToastMessage(context, "图片已保存" + dirFile.getPath(), 100);
                }
            });

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveFile(Bitmap bm, String fileName) throws IOException {
        final File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File myCaptureFile = new File(Constants.STORAGE_PICTURE,
                System.currentTimeMillis() + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                UIHelper.ToastMessage(context, "图片已保存" + dirFile.getPath(), 100);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != bmp && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        List<View> mListViews;

        public MyPagerAdapter(List<View> listViews) {
            this.mListViews = listViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public int getCount() {
            if (mListViews != null) {
                // 返回一个比较大的数字
                return mListViews.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

    }

    ;

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            setShowImage(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setShowImage(int currentItem) {
        // 先加载默认图片
        SoftReferenceImageView tempImageview = new SoftReferenceImageView(context);
//		PhotoView tempImageview = new PhotoView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tempImageview.setDefaultImage(R.drawable.icon_loadings);
//		tempImageview.setImageBitmap(bmp);
        ll_viewArea[currentItem].addView(tempImageview, lp);

        btn_zoomin[currentItem].setEnabled(false);
        btn_zoomout[currentItem].setEnabled(false);

        int nowNumber = currentItem + 1;
        String msg = nowNumber + "/" + pictureUrlLists.size();
//		UIHelper.ToastMessage(context, msg, 1);
        tv_title.setText(msg);

        if (null != intent) {
            file_path = pictureUrlLists.get(currentItem);
            if (!TextUtils.isEmpty(file_path)) {

//				file_path = "file://" + file_path;
                ll_viewArea[currentItem].removeAllViews();
                viewArea = new ViewArea(context, file_path); // 自定义布局控件，用来初始化并存放自定义imageView
                ll_viewArea[currentItem].addView(viewArea, parm);
                btn_zoomin[currentItem].setEnabled(true);
                btn_zoomout[currentItem].setEnabled(true);
                if (file_path.startsWith("/"))
                    btn_rotate[currentItem].setVisibility(View.INVISIBLE);
                else
                    btn_rotate[currentItem].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            finish();
        }
        if (v.getId() == R.id.btn_zoomin) {
            if (null != viewArea.touchView && viewArea.touchView.getWidth() > screenW) {
                viewArea.touchView.setScale(TouchImageView.scale,
                        TouchImageView.SMALLER);
            }
        }
        if (v.getId() == R.id.btn_zoomout) {
            if (null != viewArea.touchView && viewArea.touchView.getWidth() <= screenW * 4.0f) {
                viewArea.touchView.setScale(TouchImageView.scale,
                        TouchImageView.BIGGER);
            }
        }
        if (v.getId() == R.id.btn_rotate) {
            CommonAlertDialog alertDialog = new CommonAlertDialog(this);
            alertDialog.setOnMenuClickListener(onUpDownClickListener);
            alertDialog.show();
            alertDialog.setContent("是否保存图片？");
        }
        if (v.getId() == R.id.ivLeft) {
            finish();
        }

    }

    @Override
    public void onCreate(Bundle bundle) {
        setStatusBar = false;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
    }

    @Override
    public int layoutId() {
        return R.layout.change_picture_act;
    }



    @Override
    protected void initUIData() {
        // TODO Auto-generated method stub
        File nomedia = new File(Constants.STORAGE_PATH + "/.nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bmp = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.icon_loadings);

        parm = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        // viewArea = new ViewArea(this, bmp); // 自定义布局控件，用来初始化并存放自定义imageView

        screenW = this.getWindowManager().getDefaultDisplay().getWidth();
        screenH = this.getWindowManager().getDefaultDisplay().getHeight();

        intent = getIntent();
        String typeId = intent.getStringExtra(Common.KEY_CHANGE_TYPE_ID);
        if ("only".equalsIgnoreCase(typeId)) {
            String onlyUrl = intent.getStringExtra(Common.CHECK_FILE_PATH);
            pictureUrlLists.add(onlyUrl);
        } else {
            nowPicNumber = intent.getIntExtra(Common.KEY_PIC_NUMBER, 0);
            //获取传递过来的图片地址集合
            pictureUrlLists = intent.getStringArrayListExtra(Common.CHECK_FILE_PATH);
        }

        ll_viewArea = new LinearLayout[pictureUrlLists.size()];
        btn_back = new ImageView[pictureUrlLists.size()];
        btn_zoomin = new ImageView[pictureUrlLists.size()];
        btn_zoomout = new ImageView[pictureUrlLists.size()];
        btn_rotate = new ImageView[pictureUrlLists.size()];
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        tv_title = (TextView) findViewById(R.id.tvTitle);
        layoutHead = (RelativeLayout) findViewById(R.id.head);
        mViewPager = (ViewPager) findViewById(R.id.viewPager1);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        layoutHead.setBackgroundColor(getResources().getColor(R.color.transparent));

        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        for (int i = 0; i < pictureUrlLists.size(); i++) {
            View view = mInflater.inflate(R.layout.change_picture_activity, null);
            ll_viewArea[i] = (LinearLayout) view.findViewById(R.id.ll_viewArea);
            btn_back[i] = (ImageView) view.findViewById(R.id.btn_back);
            btn_zoomin[i] = (ImageView) view.findViewById(R.id.btn_zoomin);
            btn_zoomout[i] = (ImageView) view.findViewById(R.id.btn_zoomout);
            btn_rotate[i] = (ImageView) view.findViewById(R.id.btn_rotate);

            btn_back[i].setOnClickListener(this);
            btn_zoomin[i].setOnClickListener(this);
            btn_zoomout[i].setOnClickListener(this);
            btn_rotate[i].setOnClickListener(this);
            ivLeft.setOnClickListener(this);

            listViews.add(view);
        }

        mViewPager.setAdapter(new MyPagerAdapter(listViews));
        mViewPager.setCurrentItem(nowPicNumber);

        setShowImage(nowPicNumber);
    }



}