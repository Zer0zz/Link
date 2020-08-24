package com.self.link.register;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.baseModle.BaseResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description：
 * author：Administrator on 2020/6/16 18:58
 */
public class RegisterModel extends BaseModel {
    GetCodeListener mGetCodeListener;
    static final int reqId = 10001;
    private static final String TAG = RegisterModel.class.getSimpleName();
    private static final String sendCode_url = "http://lixingrui.club/api/system/sendCode/";

    public RegisterModel() {

    }


    public void getCode(String phone, GetCodeListener listener) {
        mGetCodeListener = listener;
        Map params = new HashMap();
        params.put("isRegister", "Y");
        params.put("phone", phone);
        netReqModleNew.postJsonHttp(sendCode_url, reqId, params, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String o) {
                if (!TextUtils.isEmpty(o)) {
                    BaseResponse result = JSONObject.parseObject(o, BaseResponse.class);
                    if (mGetCodeListener != null) {
                        mGetCodeListener.onSuccessful(result.getCode()+"");
                    }
                }
            }

            @Override
            public void onFaild(int id, String o, String err) {

                Log.e("TAG", err);
                if (mGetCodeListener != null) {
                    mGetCodeListener.onfialed();
                }
            }
        });
    }

    interface GetCodeListener {
        void onSuccessful(String code);

        void onfialed();
    }
}
