package com.self.link.cancle_tuanke;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.CANCEL_THECOURSE_ID;

public class CancleTuankePersenter extends BasePresenter<CancleTuankeVIew> {
    public void cancelTheCourse(String fitnessId, String courseId, String seatId) {
        Map map = new HashMap();
        map.put("courseId", courseId);
        map.put("courseSeatId", seatId);
        map.put("fitnessId", fitnessId);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.cancelTheCourse, CANCEL_THECOURSE_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                if (response.isSuccess()) {
                    getMvpView().showErrMsg("取消选课成功");
                    getMvpView().onSuccess();
                }else {
                    getMvpView().showErrMsg("取消选课失败"+response.getMsg());
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                getMvpView().showErrMsg("取消选课失败"+err);

            }
        });

    }
}
