package com.self.link.coach;

import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.coach.body.CoachDetailBody;
import com.self.link.coach.body.CoachScheduleBody;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.self.link.base.Constant.RequsetCode.COACHDETAIL_ID;

/**
 * description：
 * author：Administrator on 2020/5/31 10:32
 */
public class CoachPersenter extends BasePresenter<CoachIView> {

    private CoachModel coachModel;

    public CoachPersenter() {
        coachModel = new CoachModel();

    }

    //获取本周的数据
    public void getCoachDetail(String coachId) {
        coachModel.getCoachDetail(coachId,null,null, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == COACHDETAIL_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        CoachDetailBody coachDetail = JSONObject.parseObject(baseResponse.getRe(), CoachDetailBody.class);
                        EventBus.getDefault().post(coachDetail);
                        List<CoachScheduleBody> coachSchedules = JSONObject.parseArray(coachDetail.coachScheduleDtoList, CoachScheduleBody.class);
                        getMvpView().setScheduleData(coachSchedules);
                    } else {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }

                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == COACHDETAIL_ID) {
                    getMvpView().showErrMsg(err);
                }
            }
        });
    }
    //获取下周的数据
    public void getCoachDetail(String coachId,String startDate,String endDate) {
        coachModel.getCoachDetail(coachId,startDate, endDate,new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == COACHDETAIL_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        CoachDetailBody coachDetail = JSONObject.parseObject(baseResponse.getRe(), CoachDetailBody.class);
                        EventBus.getDefault().post(coachDetail);
                        List<CoachScheduleBody> coachSchedules = JSONObject.parseArray(coachDetail.coachScheduleDtoList, CoachScheduleBody.class);
                        getMvpView().setScheduleData(coachSchedules);
                    } else {
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }

                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == COACHDETAIL_ID) {
                    getMvpView().showErrMsg(err);
                }
            }
        });
    }
}
