package com.self.link.personal;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.base.Constant;
import com.self.link.base.UploadImgBack;
import com.self.link.base.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.logout;
import static com.self.link.base.Constant.RequsetCode.LOGOUT_ID;
import static com.self.link.base.Constant.RequsetCode.UPDATEUSERINFO;
import static com.self.link.base.Constant.RequsetCode.UPLOAD_IMAGEBASE64_ID;
import static com.self.link.base.Constant.SpKeyName.phone;
import static com.self.link.base.Constant.SpKeyName.realName;
import static com.self.link.base.Constant.SpKeyName.userId;

/**
 * description：
 * author：Administrator on 2020/5/24 16:19
 */
public class PersonalInfoPersenter extends BasePresenter<PersonalInfoIView> {

    public void upLoadImg(String base64Str, String fileName) {
        Map map = new HashMap();
        map.put("base64Str", base64Str);
        map.put("fileName", fileName);
        map.put("thuSizeX", 180);
        map.put("thuSizeY", 180);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.uploadImageBase64, UPLOAD_IMAGEBASE64_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (UPLOAD_IMAGEBASE64_ID == id) {
                    Log.e("tag", res);
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        UploadImgBack back = JSONObject.parseObject(baseResponse.getRe(), UploadImgBack.class);
                        UserInfo userInfo = getMvpView().getUserInfo();
                        userInfo.headPortrait = back.path;
                        updateUserInfo(userInfo);
                    } else {//失败

                    }

                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (UPLOAD_IMAGEBASE64_ID == id) {

                }
            }
        });
    }

    public void updateUserInfo(UserInfo userInfo) {
        Map map = new HashMap();
        map.put("dateOfBirth", userInfo.dateOfBirth);
        String headPortrait = userInfo.headPortrait;
        if (headPortrait.contains(Constant.RequestUrl.imageBaseUrl)){
            headPortrait = userInfo.headPortrait.replace(Constant.RequestUrl.imageBaseUrl,"");
        }

        map.put("headPortrait", headPortrait);
        map.put("name", userInfo.name);
        map.put("realName ", userInfo.realName);
        map.put("sex", userInfo.sex);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.updateUserInfo, UPDATEUSERINFO, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (UPDATEUSERINFO == id) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        if (baseResponse.getRe() == "true") {
                            EventBus.getDefault().postSticky(userInfo);
//                            SPUtils.saveStringValue(userId, userInfo.id);
//                            SPUtils.saveStringValue(realName, userInfo.realName);
//                            SPUtils.saveStringValue(phone, userInfo.phone);
                        }
                    } else {//失败

                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (UPDATEUSERINFO == id) {

                }
            }
        });
    }

    public void logout() {
        Map map = new HashMap();
        new BaseModel().netReqModleNew.postJsonHttp(logout, LOGOUT_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id==LOGOUT_ID){
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if(baseResponse.isSuccess()){
                        getMvpView().showErrMsg("您已退出登录！");
                        getMvpView().loginout();
                    }else {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id==LOGOUT_ID){
                    getMvpView().showErrMsg(err);
                }
            }
        });

    }
}
