package com.self.link.main.me;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.base.Constant;
import com.self.link.base.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.USERINFO;
import static com.self.link.base.Constant.SpKeyName.userId;

/**
 * <p>类说明</p>
 *
 * @author zhangchenglin
 * @date 2018/9/28 10:04
 * @Description
 */
public class MeMvpModel extends BaseModel {
    MePresenter mPresenter;
    private static final String TAG = "MeMvpModel";
    private MePresenter mePresenter;

    UserInfo mUserInfo;

    public MeMvpModel(MePresenter presenter) {
        this.mPresenter = presenter;
    }

    void updateUserInfo() {

        Map map = new HashMap();
//        map.put("sessionId", SPUtils.getStringValue(Constant.SpKeyName.SessionId));
        map.put("dateOfBirth", mUserInfo.dateOfBirth);
        map.put("headPortrait", mUserInfo.headPortrait);
        map.put("name", mUserInfo.name);
        map.put("sex", mUserInfo.sex);
        netReqModleNew.postJsonHttp(Constant.RequestUrl.updateUserInfo, Constant.RequsetCode.UPDATEUSERINFO, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String o) {

            }

            @Override
            public void onFaild(int id, String o, String err) {

            }
        });
    }


}
