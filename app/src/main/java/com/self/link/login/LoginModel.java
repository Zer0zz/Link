package com.self.link.login;


import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.baseUtils.Constants;
import com.self.link.base.Constant;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.SENDCODEID;

public class LoginModel extends BaseModel {
    private static final String TAG = LoginModel.class.getSimpleName();

    public LoginModel() {

    }

    public void sendCode2phone(String isRegister, String phoneNo, OnHttpCallBack onHttpCallBack) {
        Map<String, String> para = new HashMap<>();
        para.put("phone", phoneNo);
        para.put("isRegister", isRegister);
        OnHttpCallBack mOnHttpCallBack;
        if (onHttpCallBack != null) {
            mOnHttpCallBack = onHttpCallBack;
        } else {
            mOnHttpCallBack = new OnHttpCallBack<String>() {

                @Override
                public void onSuccessful(int id, String baseResponse) {

                }

                @Override
                public void onFaild(int id, String s, String err) {

                }
            };
        }
        netReqModleNew.postJsonHttp(Constant.RequestUrl.sendCode, SENDCODEID, para, mOnHttpCallBack);
    }

}
