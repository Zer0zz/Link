package com.self.link.login;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.base.Constant;
import com.self.link.firstin.FirstInActivity;
import com.self.link.main.MainActivity;
import com.self.link.utils.CommUtils;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.SECURITYCODE_LOGIN_ID;
import static com.self.link.base.Constant.RequsetCode.SENDCODEID;
import static com.self.link.base.Constant.SpKeyName.SessionId;
import static com.self.link.base.Constant.SpKeyName.userId;
import static com.self.link.base.Constant.SpKeyName.userName;

/**
 *
 */
public class LoginPersenter extends BasePresenter<LoginIView> {
    final LoginModel model;


    public LoginPersenter() {
        model = new LoginModel();
    }


    public void setCode2Phone() {

        String phoneNo = getMvpView().getPhoneNo();
        if (!TextUtils.isEmpty(phoneNo)) {
            if (!CommUtils.isPhoneNum(phoneNo)) {
                getMvpView().showErrMsg("请输入正确的手机号码");
                return;
            }
        } else {
            getMvpView().showErrMsg("请输入手机号码");
            return;
        }


        model.sendCode2phone("N", phoneNo, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (SENDCODEID == id) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    } else {
                        getMvpView().resetGetCodeTv();
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (SENDCODEID == id) {
                    getMvpView().showErrMsg(err);
                    getMvpView().resetGetCodeTv();
                }
            }
        });

    }

    //验证码登录
    public void securityCodeLogin(String phone, String code) {
        Map map = new HashMap();
        map.put("phone", phone);
        map.put("code", code);
        model.netReqModleNew.postJsonHttp(Constant.RequestUrl.securityCodeLogin, SECURITYCODE_LOGIN_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (!TextUtils.isEmpty(res) && id == SECURITYCODE_LOGIN_ID) {
                    BaseResponse result = JSONObject.parseObject(res, BaseResponse.class);
                    if (result.isSuccess()) {
                        LoginRe loginRe = JSONObject.parseObject(result.getRe(), LoginRe.class);
                        SPUtils.saveStringValue(SessionId, loginRe.getSessionId());
                        SPUtils.saveStringValue(userId, loginRe.getUserId());
                        SPUtils.saveStringValue(userName, loginRe.getName());
                        getMvpView().loginSuccess();
                    }else {
                        getMvpView().showErrMsg(result.getMsg());
                    }

                }
            }

            @Override
            public void onFaild(int id, String res, String err) {

            }
        });
    }


}








