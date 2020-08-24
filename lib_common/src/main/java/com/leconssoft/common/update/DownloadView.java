package com.leconssoft.common.update;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;


import com.leconssoft.common.R;
import com.leconssoft.common.base.BaseActivity;
import com.leconssoft.common.baseUtils.NetIOUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 下载视图 功能： 1.显示下载信息：如本地版本，服务器版本 2.下载控制：是否下载
 *
 * @author zouxs
 */

public class DownloadView extends BaseActivity {
    private Activity mActivity;
    private static String tag = "DownloadView";
    private VersionInfo mInfo;

    private ImageView img_back = null;
    private TextView tv_title = null;
    private Button bt_commit = null;

    private ProgressBar progressBar;// 进度条
    private TextView progress_text, server_des;
    private DownloadTask downloadTask;// 下载线程
    private Button btn_confirm, btn_cancel;
    private String apkName;// apk文件名

    private RemoteViews view = null;
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
     * 从 本地取新版本信息
     */
    private void getNewVersionInfo() {
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("KYT_VersionInfo"));
            mInfo = (VersionInfo) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initApkFile() {
        apkName = "android.apk";
        File tempApk = new File(getFilesDir(), apkName);
        if (tempApk.exists()) {
            tempApk.delete();
            Log.e(tag, "tempApk is deleted");
        }
    }

    private void initTitleView() {
        tv_title = (TextView) findViewById(R.id.tvTitle);
        img_back = (ImageView) findViewById(R.id.ivLeft);
        tv_title.setText(R.string.str_version_upgrade);
        img_back.setVisibility(View.VISIBLE);

        tv_title.setOnClickListener(buttonclick);
        img_back.setOnClickListener(buttonclick);
    }

    private void setupView() {

        initTitleView();

        progressBar = (ProgressBar) findViewById(R.id.download_progress);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(buttonclick);
        btn_cancel.setOnClickListener(buttonclick);

        server_des = (TextView) findViewById(R.id.server_des);
        progress_text = (TextView) findViewById(R.id.progress_text);
        TextView updateDesc = (TextView) findViewById(R.id.updateDesc);
        if (mInfo != null) {
            StringBuilder desc = new StringBuilder("当 前 版 本 ：").append(mInfo.localVersion).append("\n");
            desc.append("服务器版本：").append(mInfo.versionName);
            updateDesc.setText(desc);
            server_des.setText(mInfo.versionDesc);

            if ("1".equals(mInfo.forceUpdate)) { // 强制升级
                startUpgradeTask();
            }
        }

        // manager = (NotificationManager)
        // getSystemService(NOTIFICATION_SERVICE);
        view = new RemoteViews(getPackageName(), R.layout.progress_notice);
        Intent intent = new Intent(this, DownloadView.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//         notification.contentView = view;
//         notification.contentIntent = pIntent;
//         // 通知的图标必须设置(其他属性为可选设置),否则通知无法显示
//         notification.icon = R.drawable.ic_launcher;
        view.setImageViewResource(R.id.image, R.drawable.icon);// 起一个线程用来更新progress
    }

    private OnClickListener buttonclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btn_confirm) {
                if (!NetIOUtils.isNetworkAvailable(mActivity)) {
                    showAlertDialog(1);
                    return;
                }
                startUpgradeTask();
            } else if (i == R.id.tvTitle || i == R.id.ivLeft || i == R.id.btn_cancel) {
                onBackPressed();
            }
        }
    };

    // 提示网络连接失败：1为网络未连接;2为请求接口失败
    private void showAlertDialog(int showMessageType) {
        CommonAlertDialog alertDialog = new CommonAlertDialog(mActivity);
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
        alertDialog.setMenuTitle(mActivity.getString(R.string.alert_tips_title));
        if (showMessageType == 1) {
            alertDialog.setContent(mActivity.getString(R.string.alert_dialog_net_fail));
        } else {
            alertDialog.setContent("网络已断开,请检查网络连接。");
        }

        alertDialog.setBtnVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
    }

    private void startUpgradeTask() {
        downloadTask = new DownloadTask();
        if (mInfo != null && null != mInfo.updatePath) {
            // TODO: 2018/10/10 这里需要放app下载地址 启动异步下载
//            if (Constants.isDebug) {
//                downloadTask.execute(mInfo.getUpdatePath());
//            } else {
//                downloadTask.execute(mInfo.getUpdatePath());
//            }
        }
    }

    private void cacelUpgradTask() {
        if (downloadTask != null) {
            downloadTask.cancel(true);
            downloadTask.isCancelled = true;
        }
    }

    @Override
    public void onBackPressed() {
        cacelUpgradTask();
        finish();
        if (isTaskRoot()) {
            System.exit(0);
        }
        super.onBackPressed();
    }


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
            progressBar.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.GONE);
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
                client = AndroidHttpClient.newInstance(DownloadView.this, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;)");
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
                out = new DataOutputStream(openFileOutput(apkName, Context.MODE_PRIVATE));// 使用MODE_WORLD_READABLE才能正常安装

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
            progress_text.setText("已下载" + values[1] / 1024 + "K/" + values[2] / 1024 + "K   " + values[0] + "%");
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
                puri = FileProvider.getUriForFile(mActivity, "com.hbszjt.scan.FileProvider", new File(getFilesDir(), apkName));
            } else {
                puri = Uri.fromFile(new File(getFilesDir(), apkName));
                apkintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            apkintent.setDataAndType(puri, "application/vnd.android.package-archive");
            startActivity(apkintent);
            finish();
        }
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


    @Override
    protected int layoutId() {
        return R.layout.progress_update;
    }

    @Override
    protected void initUIData(Bundle bundle) {
        mActivity = DownloadView.this;
        mInfo = (VersionInfo) getIntent().getSerializableExtra("versionInfo");
        if (mInfo == null) {
            getNewVersionInfo();
        }
        initApkFile();
        setupView();
    }

}