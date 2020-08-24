package com.leconssoft.common.update;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.leconssoft.common.NetService.NetReqModleNew;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.baseUtils.baseModle.BaseResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 版本升级工具
 *
 * @author ynb
 */
public class UpgradeUtils {

    private static final String tag = UpgradeUtils.class.getSimpleName();
    private static ProgressDialog progressDialog;
    static UpdateDialog updateDialog = null;

    /**
     * 检测新版本
     *
     * @param ctx
     */
    public static void checkNewVersion(final Activity ctx) {
        checkNewVersion(ctx, false);
    }

    /**
     * 检测新版本
     *
     * @param ctx
     * @param isShowNoUpdateTip 是否显示请求进度框
     */
    public static void checkNewVersion(final Activity ctx, final boolean isShowNoUpdateTip) {

//        new Thread() {
//            @Override
//            public void run() {
                try {
                    if (isShowNoUpdateTip) {
                        ctx.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog = ProgressDialog.show(ctx, null, "正在检查更新，请稍候...", true, true);
                            }
                        });
                    }
                    try {
                        Map<String, String> para = new HashMap<>();
                        para.put("type", "GetAppbb");
                        para.put("appkey", Common.APP_KEY);
                        NetReqModleNew.getInstance().getBandParmHttp(0, para, new OnHttpCallBack<String>() {
                            @Override
                            public void onSuccessful(int id, String clientRust) {
                                // TODO Auto-generated method stub
                                try {
                                    final AppInfo mInfo = JSON.parseObject(clientRust.toString(), AppInfo.class);
                                    PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
                                    if (isShowNoUpdateTip) {
                                        ctx.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!ctx.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                                    }

                                    if (mInfo != null && Integer.valueOf(mInfo.getBbbh().replace(".", "").trim()) > info.versionCode) {
                                        final VersionInfo versionInfo = new VersionInfo();
                                        versionInfo.setForceUpdate(mInfo.getSfqzgx());
                                        versionInfo.setPublishDate(mInfo.getBbfbsj());
                                        versionInfo.setUpdatePath(mInfo.getApkurl());
                                        versionInfo.setVersionDesc(mInfo.getBbsm());
                                        versionInfo.setLocalVersion(info.versionName);
                                        versionInfo.setVersionName(mInfo.getBbmc());
                                        ctx.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showUpgradeDialog(ctx, versionInfo);
                                            }
                                        });
                                        saveNewVersionInfo(ctx, versionInfo);
                                    } else {
                                        if (isShowNoUpdateTip) {
                                            ctx.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showNoUpdateTip(ctx);
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }

                            @Override
                            public void onFaild(int id, String clientRust, String err) {
                                switch (0) {
                                    case 0:
                                        break;
                                }
                                if (isShowNoUpdateTip) {
                                    ctx.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //关闭提示更新请求的弹框
                                            if (!ctx.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                            ctx.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showNoUpdateTip(ctx);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.d(tag, " " + e.getMessage());
                }
//            }
//        }.start();

    }

    /**
     * 将版本信息持久化
     *
     * @param info
     */
    private static void saveNewVersionInfo(Activity ctx, VersionInfo info) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(ctx.openFileOutput("VersionInfo", Context.MODE_PRIVATE));
            out.writeObject(info);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从 本地取新版本信息
     */
    public static VersionInfo getNewVersionInfo(final Activity ctx) {
        try {
            ObjectInputStream in = new ObjectInputStream(ctx.openFileInput("VersionInfo"));
            VersionInfo info = (VersionInfo) in.readObject();
            in.close();
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 发送新版本更新通知
     *
     * @param versionInfo
     */
    private static void notifycation(final Activity ctx, final VersionInfo versionInfo) {
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(ctx, DownloadView.class);
        notificationIntent.putExtra("versionInfo", versionInfo);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String titleText = "发现有新版本"; // 通知标题文本()
        String contentText = "点击可下载最新版本升级";

        int iconId = ctx.getApplicationContext().getApplicationInfo().icon;

        Notification notification = new Notification(iconId, titleText, System.currentTimeMillis());
//		notification.setLatestEventInfo(ctx, titleText, contentText,
//				contentIntent);
        mNotificationManager.notify(iconId, notification);
    }

    /**
     * 显示升级对话框
     *
     * @param activity
     * @param versionInfo
     */
    private static void showUpgradeDialog(final Activity activity, final VersionInfo versionInfo) {
        if (activity instanceof Activity) {
            if (((Activity) activity).isFinishing()) {
                return;
            }
        }
        String des = "";
        try {
            des = new String((versionInfo.versionDesc).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (((Activity) activity).isFinishing()) {
            return;
        }
        int versionCode = 0;
        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ("1".equals(versionInfo.forceUpdate) || (versionCode != 0 ? (versionCode < (int) versionInfo.supportAndoridVersion) : false)) {
            // 强制升级
            if (updateDialog != null) {
                updateDialog = null;
            }
            updateDialog = new UpdateDialog(activity, versionInfo, des, 1);
            updateDialog.setCacleable(false);
            updateDialog.show();
        } else {
            if (updateDialog != null) {
                updateDialog = null;
            }
            updateDialog = new UpdateDialog(activity, versionInfo, des, 2);
            updateDialog.setCacleable(false);
            updateDialog.show();
        }

    }

    private static void showNoUpdateTip(final Activity activity) {
        if (activity instanceof Activity) {
            if (((Activity) activity).isFinishing()) {
                return;
            }
        }

        CommonAlertDialog alertDialog = new CommonAlertDialog(activity);
        alertDialog.show();
        alertDialog.setBtnVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
        alertDialog.setMenuTitle("升级提醒");
        alertDialog.setContent("您的版本已是最新版本！");
    }

}
