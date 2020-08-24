package com.self.link.personal.change_info;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.base.Constant;
import com.self.link.base.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.UPDATEUSERINFO;
import static com.self.link.base.Constant.SpKeyName.phone;
import static com.self.link.base.Constant.SpKeyName.realName;
import static com.self.link.base.Constant.SpKeyName.userId;

public class ChangePersonInfoPersenter extends BasePresenter<ChangePersonInfoView> {

    public void updateUserInfo(UserInfo userInfo) {
        Map map = new HashMap();
        map.put("dateOfBirth", userInfo.dateOfBirth);
        String headPortrait = userInfo.headPortrait;
        if (headPortrait.contains(Constant.RequestUrl.imageBaseUrl)){
            headPortrait = userInfo.headPortrait.replace(Constant.RequestUrl.imageBaseUrl,"");
        }
        map.put("headPortrait", headPortrait);
        map.put("name", userInfo.name);
        map.put("realName", userInfo.realName);
        map.put("sex", userInfo.sex);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.updateUserInfo, UPDATEUSERINFO, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (UPDATEUSERINFO == id) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        if (baseResponse.getRe() == "true") {
                            EventBus.getDefault().postSticky(userInfo);
                            getMvpView().uploadSuccess();
//                            SPUtils.saveStringValue(userId, userInfo.id);
//                            SPUtils.saveStringValue(realName, userInfo.realName);
//                            SPUtils.saveStringValue(phone, userInfo.phone);
                        }
                    } else {//失败
                        getMvpView().uploadFialed();
                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (UPDATEUSERINFO == id) {
                    getMvpView().showErrMsg(err);
//                    getMvpView().uploadFialed();
                }
            }
        });
    }
}
