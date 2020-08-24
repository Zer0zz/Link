package com.self.link.selectseat;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.fitness.body.Attachment;
import com.self.link.selectseat.body.SeatBody;
import com.self.link.selectseat.body.SeatBodyList;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.CANCEL_THECOURSE_ID;
import static com.self.link.base.Constant.RequsetCode.CURRICULA_VARIABLE_ID;

/**
 * description：
 * author：Administrator on 2020/5/30 22:13
 */
public class SelectSeatPersenter extends BasePresenter<SelectSeatIView> {


    SelectSeatModel model = new SelectSeatModel();

    public void getCourseSeatData(String courseId) {
        OnHttpCallBack callBack = new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == Constant.RequsetCode.COURSESEAT_LIST_ID) {
                    try {
                        BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                        if (response.isSuccess()) {
                            SeatBodyList bodyList = JSONObject.parseObject(response.getRe(), SeatBodyList.class);
                            EventBus.getDefault().post(bodyList);
                        }
                        //                        String presentation = bodyList.presentation;
//                        Attachment attachment = JSONObject.parseObject(bodyList.attachment, Attachment.class);
//                        List<SeatBody> seatBodys = JSONObject.parseArray(bodyList.courseSeatList, SeatBody.class);

                    } catch (Exception e) {
                        getMvpView().showErrMsg("解析座位数据失败！");
                        Log.e("SelectSeatPersenter", e.getMessage());
                    }


                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == Constant.RequsetCode.COURSESEAT_LIST_ID) {
                    getMvpView().showErrMsg(err);
                }
            }
        };
        model.loadData(courseId, callBack);
    }


    public void curriculaVariable(String fitnessId, String courseId, String seatId,String seatSerialNumber) {
        Map map = new HashMap();
        map.put("courseId", courseId);
        map.put("courseSeatId", seatId);
        map.put("fitnessId", fitnessId);
        model.netReqModleNew.postJsonHttp(Constant.RequestUrl.curriculaVariable, CURRICULA_VARIABLE_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                if (response.isSuccess()) {
                    getMvpView().showErrMsg("团课选座成功");
                    getMvpView().selectSuccess(seatSerialNumber);
                }else {
                    getMvpView().showErrMsg("团课选座失败"+response.getMsg());
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                getMvpView().showErrMsg("团课选座失败"+err);
            }
        });

    }

    public void cancelTheCourse(String fitnessId, String courseId, String seatId,String seatSerialNumber) {
        Map map = new HashMap();
        map.put("courseId", courseId);
        map.put("courseSeatId", seatId);
        map.put("fitnessId", fitnessId);
        model.netReqModleNew.postJsonHttp(Constant.RequestUrl.cancelTheCourse, CANCEL_THECOURSE_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                BaseResponse response = JSONObject.parseObject(res, BaseResponse.class);
                if (response.isSuccess()) {
                    getMvpView().showErrMsg("取消选课成功");

                    getMvpView().cancleSuccess(seatSerialNumber);
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
