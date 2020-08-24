package com.self.link.mycenter.personal;

import android.location.Location;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.mycenter.dto.PersonalCourseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.imageBaseUrl;
import static com.self.link.base.Constant.RequsetCode.CANCEL_THERESERVATION_ID;
import static com.self.link.base.Constant.RequsetCode.MYCOACH_ID;

public class PersonalCourseModel extends BaseModel {

    public void getPersonalCourseData(int pageNum, int pageSize, String fitnessId, Location location, OnGetPersonalCourseDataListener listener) {
        Map map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("fitnessId", fitnessId);
        if (location != null) {
            map.put("longitude", location.getLongitude());
            map.put("latitude", location.getLatitude());
        } else {
            map.put("longitude", 1);
            map.put("latitude", 1);
        }


        netReqModleNew.getBandParmHttp(Constant.RequestUrl.myCoach, MYCOACH_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == MYCOACH_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                        boolean hasNextPage = pageObject.hasNextPage;
                        boolean isFirstPage = pageObject.isFirstPage;
                        int pageNum = pageObject.pageNum;

                        List<PersonalCourseDto> personalCourseDtos = JSONObject.parseArray(pageObject.list, PersonalCourseDto.class);
                        if (personalCourseDtos != null && personalCourseDtos.size() > 0) {
                            for (PersonalCourseDto personalCourseDto : personalCourseDtos) {
//                            fitnessBody.surfacePlot = imageBaseUrl + fitnessBody.surfacePlot;
                                personalCourseDto.headPortrait = imageBaseUrl + personalCourseDto.headPortrait;
                                personalCourseDto.personalImage = imageBaseUrl + personalCourseDto.personalImage;
                            }
                            if (listener != null)
                                listener.onComplited(hasNextPage, personalCourseDtos, isFirstPage, pageNum);
                        }

                    } else {
                        if (listener != null)
                            listener.onfailed(baseResponse.getMsg());
                    }
                }

            }

            @Override
            public void onFaild(int id, String o, String err) {
                if (id == MYCOACH_ID) {
                    if (listener != null)
                        listener.onfailed(err);
                }
            }
        });

    }

    public void cancelThereservation(String coachScheduleId,@NonNull OnOperatePersonaltrainerListener listener) {
        Map map = new HashMap();
        map.put("coachScheduleId", coachScheduleId);
        netReqModleNew.postJsonHttp(Constant.RequestUrl.cancelTheReservation, CANCEL_THERESERVATION_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == CANCEL_THERESERVATION_ID) {
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

    public interface OnGetPersonalCourseDataListener {
        void onComplited(boolean hasNextPage, List<PersonalCourseDto> personalCourseDtos, boolean isFirstPage, int currentPage);

        void onfailed(String errMsg);
    }

    public interface OnOperatePersonaltrainerListener {
        void oncomplited();

        void onfailed(String err);
    }


}
