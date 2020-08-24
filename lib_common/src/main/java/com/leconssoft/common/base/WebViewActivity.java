package com.leconssoft.common.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.leconssoft.common.R;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.baseUtils.UIHelper;
import com.leconssoft.common.dialog.ShareSaveDialog;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.RequiresApi;


/**
 * 统一的webview的activity
 */
public class WebViewActivity extends BaseHeadActivity {
    protected WebView my_webview;
    protected String loadurl = "";
    protected String title = "";
    String sourceData = "";
    boolean openHome = false;
    private int SYNC_MESSAGE = 11;
    private long SYNC_LATER_INTERVAL = 50;
    private boolean imShare = false;
    private String imShareContent="";
    private String imShareImg="";
    private int imShareType=1;

    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;



    @Override
    protected void setIvLeftOnclick() {
        super.setIvLeftOnclick();
        if (my_webview.canGoBack()) {
            my_webview.goBack();
        } else {
            finish();
        }
    }

    @Override
    public int layoutId() {
        return R.layout.web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initUIData() {
        my_webview=findViewById(R.id.my_webview);
        loadurl = getIntent().getStringExtra(Common.WEBVIEW_LOADURL);
        title = getIntent().getStringExtra(Common.WEBVIEW_TITLE);
        openHome = getIntent().getBooleanExtra(Common.WEBVIEW_ADVERTISEMENT, false);
        imShare = getIntent().getBooleanExtra("imShare", false);
        imShareType=getIntent().getIntExtra("imshareType",1);
        if (imShare){
            if(loadurl != null && !loadurl.startsWith("http://") && !loadurl.startsWith("https://")){
                loadurl = String.format("http://%s", loadurl);
            }
            imShareContent = getIntent().getStringExtra("imShareContent");
            imShareImg = getIntent().getStringExtra("imShareImg");
        }
        if(TextUtils.isEmpty(loadurl)){
            UIHelper.ToastMessage(this, "访问地址出错！");
            finish();
        }

        if (imShare) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText("分享");
        }
        tvTitle.setText(title);
        ivRight.setVisibility(View.GONE);

        WebSettings webSettings = my_webview.getSettings();
        /*----设置WebView控件参数----*/
        webSettings.setJavaScriptEnabled(true);// 设置响应JS
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(my_webview, true);
        }
        my_webview.setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        my_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        my_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (result.getType() == WebView.HitTestResult.IMAGE_TYPE) {//判断被点击的类型为图片
                    ShareSaveDialog shareSaveDialog = new ShareSaveDialog(WebViewActivity.this, result.getExtra());
                    shareSaveDialog.show();
                }
                return false;
            }
        });
        my_webview.setWebViewClient(new WebViewClient() {// 设置WebView客户端对象
            /**
             * 重写方法，否则点击页面时WebView会重新启动系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    WebViewActivity.this.startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                hindProgress();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 让https的站点通过访问，设置为可通过证书。
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description,
                        failingUrl);
                // my_webview.loadDataWithBaseURL(null, "", "text/html",
                // "utf-8", null);// 清除之前缓存页面
                hindProgress();
            }
        });
        my_webview.setWebChromeClient(new WebChromeClient() {
            /**
             * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.e("smallbug", "运行方法 openFileChooser-1");
                // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
                mUploadCallbackBelow = uploadMsg;
                takePhoto();
            }

            /**
             * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.e("smallbug", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.e("smallbug", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * API >= 21(Android 5.0.1)回调此方法
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.e("smallbug", "运行方法 onShowFileChooser");
                // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
                mUploadCallbackAboveL = filePathCallback;
                takePhoto();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 网页加载完成
                    hindProgress();
                    view.getSettings().setBlockNetworkImage(false);
                }
            }
        });
        my_webview.setDownloadListener(new MyDownloadStart());

            //设置WEB调用android 的方法
//        my_webview.addJavascriptInterface(jsobject, "android");
        my_webview.loadUrl(loadurl);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (openHome) {
                //跳转到主页
//                startActivity(new Intent(WebViewActivity.this, .class));
                onBackPressed();
            }
            if (my_webview.canGoBack()) {
                my_webview.goBack();// 返回前一个页面
                return true;
            } else {
                onBackPressed();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    class MyDownloadStart implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            // TODO Auto-generated method stub
            //调用自己的下载方式
//          new HttpThread(url).start();
            //调用系统浏览器下载
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE) {
                // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
                if (mUploadCallbackBelow != null) {
                    chooseBelow(resultCode, data);
                } else if (mUploadCallbackAboveL != null) {
                    chooseAbove(resultCode, data);
                } else {
                    Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
                }
            }
    }






    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);


        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        Log.e("smallbug", "返回调用方法--chooseBelow");
        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("smallbug", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                Log.e("smallbug", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     * @param resultCode 选取文件或拍照的返回码
     * @param data 选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        Log.e("smallbug", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("smallbug", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                Log.e("smallbug", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }



}
