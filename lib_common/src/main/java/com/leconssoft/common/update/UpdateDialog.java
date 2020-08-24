package com.leconssoft.common.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.leconssoft.common.R;
import com.leconssoft.common.baseUtils.Constants;
import com.leconssoft.common.baseUtils.NetIOUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 版本更新对话框
 *
 * @author hxh
 * @version [versionCode="6", 2016年7月16日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UpdateDialog extends Dialog implements OnClickListener {
    private static String tag = "UpdateDialogDownload";
    Activity activity;
    private VersionInfo versionInfo;
    private TextView tvUpdate;
    private TextView updateContent;
    private ImageView ivClose;
    private String apkName;// apk文件名

    private ProgressBar progressBar;// 进度条
    private TextView progress_text;
    private DownloadTask downloadTask;// 下载线程
    private RemoteViews view = null;
    private RelativeLayout content_layout;
    private TextView appName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            view.setProgressBar(R.id.pb, 100, msg.arg1, false);
            if (msg.arg1 == 100) {
                view.setTextViewText(R.id.tv, "下载完成");
            } else {
                view.setTextViewText(R.id.tv, "下载" + msg.arg1 + "%");
                // 关键部分，如果你不重新更新通知，进度条是不会更新的
            }
        }
    };
    /**
     * apk总的字节数
     **/
    private int totalByteCount = 0;
    /**
     * 尝试建立链接的次数
     **/
    private int tryCount = 0;
    /**
     * 当前已下载字节数
     **/
    private int hasDownloadByteCount = 0;

    public UpdateDialog(Context context, VersionInfo versionInfo, String msg, int type) {
        super(context, R.style.DialogTheme);
        this.activity = (Activity) context;
        this.versionInfo = versionInfo;
        setContentView(R.layout.dialog_version_update);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        updateContent = (TextView) findViewById(R.id.ev_update_explain);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        content_layout = findViewById(R.id.content_layout);
        appName = findViewById(R.id.appName);
        updateContent.setText(msg);
        progressBar = findViewById(R.id.download_progress);
        progress_text = (TextView) findViewById(R.id.progress_text);
        if (type == 2) {
            ivClose.setVisibility(View.VISIBLE);
        }
        setWindow();
        tvUpdate.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        initApkFile();
        view = new RemoteViews(activity.getPackageName(), R.layout.progress_notice);
//         notification.contentView = view;
//         notification.contentIntent = pIntent;
//         // 通知的图标必须设置(其他属性为可选设置),否则通知无法显示
//         notification.icon = R.drawable.ic_launcher;
        view.setImageViewResource(R.id.image, R.drawable.icon);// 起一个线程用来更新progress
    }

    public void setWindow() {
        Window window = this.getWindow();
        // window.setGravity(Gravity.); // 此处可以设置dialog显示的位置

        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        LayoutParams p = window.getAttributes();
        p.width = LayoutParams.MATCH_PARENT;
        p.height = LayoutParams.MATCH_PARENT;
        window.setAttributes(p);
//		window.setWindowAnimations(R.style.normalDialogAnim);
        setCanceledOnTouchOutside(false);
    }

    public UpdateDialog setCacleable(boolean iscanCancelable) {
        this.setCancelable(iscanCancelable);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_update) {
            if (!NetIOUtils.isNetworkAvailable(activity)) {
                showAlertDialog(1);
                return;
            }
            startUpgradeTask();

        } else if (i == R.id.iv_close) {
            dismiss();

        } else {
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initApkFile() {
        apkName = "android.apk";
        File tempApk = new File(activity.getFilesDir(), apkName);
        if (tempApk.exists()) {
            tempApk.delete();
            Log.e(getClass().getName(), "tempApk is deleted");
        }
    }

    private void startUpgradeTask() {
        downloadTask = new DownloadTask();
        if (versionInfo != null && null != versionInfo.updatePath) {
            // TODO: 2018/10/10 这里需要放app下载地址 启动异步下载
            content_layout.setVisibility(View.VISIBLE);
            updateContent.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            tvUpdate.setVisibility(View.GONE);
            appName.setText(versionInfo.getVersionName());
            progress_text.setText("开始下载...");
//            NetReqModleNew.getInstance().downLoadFile(100, versionInfo.getUpdatePath(), "aps.apk", new DownLoadListener() {
//                @Override
//                public void onSuccessful(int id, Object o) {
//                    UIHelper.ToastMessage(getContext(), o.toString()+"下载成功");
//                }
//
//                @Override
//                public void onFaild(int id, Object o, String err) {
//
//                }
//
//                @Override
//                public void onProgress(long... value) {
//                    progressBar.setProgress((int)value[2]);
//                    float downSize = ((int)value[0] * 100 / 1024 / 1024) / 100;
//                    float ruesltSize = ( (int)value[1] * 100 / 1024 / 1024) / 100;
////                  progress_text.setText("已下载" + values[1] / 1024/1024 + "M/" + values[2] / 1024/1024 + "M   " + values[0] + "%");
//                    progress_text.setText("已下载" + downSize + "M/" + ruesltSize + "M   " + value[2] + "%");
//                }
//            });
            if (Constants.isDebug) {
                downloadTask.execute(versionInfo.getUpdatePath());
            } else {
                downloadTask.execute(versionInfo.getUpdatePath());
            }
        }
    }

    // 提示网络连接失败：1为网络未连接;2为请求接口失败
    private void showAlertDialog(int showMessageType) {
        CommonAlertDialog alertDialog = new CommonAlertDialog(activity);
        alertDialog.setOnMenuClickListener(new CommonAlertDialog.onMenuClickListener() {
            @Override
            public void onMenuClick(int index) {
                if (index == 2) { // 确定
                    try {
                        new Thread() {
                            public void run() {
                            }
                        }.start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else { // 取消
                }
            }
        });
        alertDialog.show();
        alertDialog.setMenuTitle(activity.getString(R.string.alert_tips_title));
        if (showMessageType == 1) {
            alertDialog.setContent(activity.getString(R.string.alert_dialog_net_fail));
        } else {
            alertDialog.setContent("网络已断开,请检查网络连接。");
        }

        alertDialog.setBtnVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
    }

    private void cacelUpgradTask() {
        if (downloadTask != null) {
            downloadTask.cancel(true);
            downloadTask.isCancelled = true;
        }
    }

    @Override
    public void dismiss() {
        cacelUpgradTask();
        super.dismiss();
    }

    /**
     * 发送进度消息
     *
     * @param progress
     */
    private void sendMsg(int progress) {
        Message msg = Message.obtain();
        msg.arg1 = progress;
        handler.sendMessage(msg);
    }

    /**
     * 下载进程
     */
    private class DownloadTask extends AsyncTask<String, Integer, Integer> {

        private boolean isCancelled;// 是否已取消

        @Override
        protected void onPreExecute() {
            if (hasDownloadByteCount > 0) {
                initApkFile();
                return;
            }
            content_layout.setVisibility(View.VISIBLE);
            updateContent.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            tvUpdate.setVisibility(View.GONE);
            appName.setText(versionInfo.getVersionName());
            progress_text.setText("开始下载...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            // 此方法只能用户HTTP协议
            AndroidHttpClient client = null;
            DataInputStream in = null;
            DataOutputStream out = null;
            int downloadSize = 0;
            try {
                client = AndroidHttpClient.newInstance(activity, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;)");
                HttpGet request = null;
                request = new HttpGet(params[0]);
                request.setHeader("Connection", "keep-alive");
                HttpResponse response = client.execute(request);
                Header contentLength = response.getFirstHeader("Content-Length");
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    in = new DataInputStream(response.getEntity().getContent());
                }
                int total = Integer.parseInt(TextUtils.isEmpty(contentLength.getValue()) ? "0" : contentLength.getValue());
                totalByteCount = total;
                out = new DataOutputStream(activity.openFileOutput(apkName, Context.MODE_PRIVATE));// 使用MODE_WORLD_READABLE才能正常安装

                byte[] buffer = new byte[1024];
                int count = 0;
                int loopCount = 0;// 循环次数

                while (!isCancelled && (count = in.read(buffer)) > 0) {

                    // 如果当前读出的字节数小于已下载的字节数，说明这是重新建立链接，无需更新已下载字节数及百分比
                    if (tryCount > 0 && downloadSize < hasDownloadByteCount) {
                        out.write(buffer, 0, count);
                        downloadSize += count;
                        continue;
                    }

                    out.write(buffer, 0, count);
                    downloadSize += count;
                    hasDownloadByteCount = downloadSize;
                    int progress = (int) ((long) downloadSize * 100 / (long) total);
                    if (++loopCount % 20 == 0 || progress == 100) {
                        publishProgress(progress, downloadSize, total);

                        sendMsg(progress);
                    }
                }
                if (isCancelled) {
                    doCancelled();
                }
            } catch (Exception e) {
                Log.d(tag, "exception:" + e.getMessage());
                e.printStackTrace();

            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return downloadSize;
        }

        /**
         * 任务取消后的处理
         */
        private void doCancelled() {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            float downSize = (values[1] * 100 / 1024 / 1024) / 100;
            float ruesltSize = (values[2] * 100 / 1024 / 1024) / 100;
//            progress_text.setText("已下载" + values[1] / 1024/1024 + "M/" + values[2] / 1024/1024 + "M   " + values[0] + "%");
            progress_text.setText("已下载" + downSize + "M/" + ruesltSize + "M   " + values[0] + "%");
        }

        @Override
        public void onPostExecute(Integer size) {// 安装apk
            if (totalByteCount > 0 && size < totalByteCount) {// 下载未完成,重新建立链接下载
                Log.d(tag, "tryCount=" + tryCount);
                // 休眠10s，再建立新的链接
                SystemClock.sleep(10000);

                tryCount++;
                startUpgradeTask();
                Log.d(tag, "tryCount=" + tryCount);
                Log.d(tag, "hasDownloadByteCount=" + hasDownloadByteCount);
                return;
            }
            // 下载完成
            Log.d(tag, "size=" + size + "====totalByteCount=" + totalByteCount);
            Intent apkintent = new Intent(Intent.ACTION_VIEW);
            Uri puri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                puri = FileProvider.getUriForFile(activity, "com.hbszjt.scan.FileProvider", new File(activity.getFilesDir(), apkName));
            } else {
                puri = Uri.fromFile(new File(activity.getFilesDir(), apkName));
                apkintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            apkintent.setDataAndType(puri, "application/vnd.android.package-archive");
            activity.startActivity(apkintent);
            activity.finish();
        }
    }
}
