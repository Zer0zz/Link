package com.self.link.flash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseActivity;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.base.UserInfo;
import com.self.link.login.LoginActivity;
import com.self.link.main.MainActivity;
import com.self.link.utils.NetInfoUtils;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.USERINFO;
import static com.self.link.base.Constant.SpKeyName.phone;
import static com.self.link.base.Constant.SpKeyName.realName;
import static com.self.link.base.Constant.SpKeyName.userId;

public class FlashActivity extends BaseActivity {

    Handler mHandler = new Handler();
    String sessionId;

    @Override
    protected int layoutId() {
        return R.layout.activity_flash;
    }

    @Override
    protected void initUIData(Bundle bundle) {
        sessionId = SPUtils.getStringValue(Constant.SpKeyName.SessionId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean netConnect = NetInfoUtils.isNetworkConnected(this);
        if (!netConnect) {
            NetInfoUtils.showSetNetworkDialog(this, new NetInfoUtils.OnSettingNetworkResult() {
                @Override
                public void goSettingNetwork() {

                }

                @Override
                public void refuseSetting() {
                    finish();
                }
            });
        } else {//联网了
            doNext();
        }

    }

    private void doNext() {
        new Thread(() -> {
            try {
                if (!TextUtils.isEmpty(sessionId)) {
                    Thread.sleep(500);
                } else {
                    Thread.sleep(800);
                }
                mHandler.post(() -> {
                    if (!TextUtils.isEmpty(sessionId)) {
                        getUserInfo(sessionId);
                    } else {
                        Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getUserInfo(String sessionId) {
        Map map1 = new HashMap();
        new BaseModel().netReqModleNew.getBandParmHttp(Constant.RequestUrl.userInfo, USERINFO, map1, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String response) {
                if (id == USERINFO) {
                    BaseResponse baseResponse = JSONObject.parseObject(response, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        UserInfo userInfo = JSONObject.parseObject(baseResponse.getRe(), UserInfo.class);
                        SPUtils.saveStringValue(userId, userInfo.id);
                        SPUtils.saveStringValue(realName, userInfo.realName);
                        SPUtils.saveStringValue(phone, userInfo.phone);
                        Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (baseResponse.getCode() == -3) {//未登录
                        Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(FlashActivity.this, "获取信息失败:" + baseResponse.getCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFaild(int id, String s, String err) {
                if (id == USERINFO) {
                    Toast.makeText(FlashActivity.this, "获取信息失败，请检查你的网络", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}