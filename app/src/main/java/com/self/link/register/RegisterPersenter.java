package com.self.link.register;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.BaseMvp.view.BaseView;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.base.Constant;
import com.self.link.utils.CommUtils;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.REGISTER;
import static com.self.link.base.Constant.RequsetCode.SENDCODEID;

/**
 * description：
 * author：Administrator on 2020/6/16 18:57
 */
public class RegisterPersenter extends BasePresenter<RegisterIView> {
    //present持有 model的引用
    private RegisterModel registerModel;

    public RegisterPersenter() {
        registerModel = new RegisterModel();
    }

    public void setCode2Phone(String phoneNo) {

        Map<String, String> para = new HashMap<>();
        para.put("phone", phoneNo);
        para.put("isRegister", "Y");

        registerModel.netReqModleNew.postJsonHttp(Constant.RequestUrl.sendCode, SENDCODEID, para, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (SENDCODEID == id) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    } else {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (SENDCODEID == id) {
                    getMvpView().showErrMsg(err);
                }
            }
        });

    }

    public void register(String name, String phone, String code) {
        getMvpView().showLodingDialog();
        Map map = new HashMap();
        map.put("name", name);
        map.put("phone", phone);
        map.put("code", code);
        registerModel.netReqModleNew.postJsonHttp(Constant.RequestUrl.register, REGISTER, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                Log.e("register_tag", res);
                if (id == REGISTER) {
                    BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                    if (response.isSuccess()) {

                        getMvpView().showErrMsg("注册成功，请登录!");
                    } else {
                        getMvpView().showErrMsg(response.getMsg());
                    }
                    getMvpView().hidLodingDialog();
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == REGISTER) {
                    getMvpView().showErrMsg(err);
                    getMvpView().hidLodingDialog();
                }
            }
        });
    }

}
