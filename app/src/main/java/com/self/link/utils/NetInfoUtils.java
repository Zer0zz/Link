package com.self.link.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

import com.leconssoft.common.update.CommonAlertDialog;

/**
 * description：
 * author：zhangCl on 2018/10/22 17:03
 */
public class NetInfoUtils {

    /**
     * 获取当前手机的网络状态
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 提示网络状态不可用，并进行设置
     */
    public static void showSetNetworkDialog(final Activity context, final OnSettingNetworkResult settingNetworkResult) {

        CommonAlertDialog alertDialog = new CommonAlertDialog(context);
        alertDialog.show();
        alertDialog.setBtnVisible(View.VISIBLE, View.GONE, View.VISIBLE);
        alertDialog.setMenuTitle("设置网络");
        alertDialog.setContent("网络错误，请检查网络状态");
        alertDialog.setOnMenuClickListener(new CommonAlertDialog.onMenuClickListener() {
            @Override
            public void onMenuClick(int index) {
                if (index == 0) {
                    if (settingNetworkResult != null)
                        settingNetworkResult.refuseSetting();
                } else if (index == 1) {
                    if (settingNetworkResult != null)
                        settingNetworkResult.goSettingNetwork();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    context.startActivity(intent);
                }
            }
        });
    }


    public interface OnSettingNetworkResult {
        void goSettingNetwork();
        void refuseSetting();
    }
}
