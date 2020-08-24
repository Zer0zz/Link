package com.self.link.course;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.mycenter.personal.PersonalCourseModel;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.SUBSCRIBE_PERSONALTRAINER_ID;

public class CourseDetailPersenter extends BasePresenter<CourseDetailView> {



    public void subscribePersonaltrainer(String coachScheduleId, PersonalCourseModel.OnOperatePersonaltrainerListener listener) {
        Map map = new HashMap();
        map.put("coachScheduleId", coachScheduleId);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.subscribePersonalTrainer, SUBSCRIBE_PERSONALTRAINER_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == SUBSCRIBE_PERSONALTRAINER_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);

                    if (baseResponse.isSuccess()) {
                        if (listener != null) {
                            listener.oncomplited();
                        }
                    } else {
                        if (listener != null) {
                            listener.onfailed(baseResponse.getMsg());
                        }

                    }
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (listener != null) {
                    listener.onfailed(err);
                }
            }
        });
    }

}
