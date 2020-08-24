package com.self.link.daka.tuanke;

import android.location.Location;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.daka.sijiao.SijiaoModel;
import com.self.link.daka.sijiao.dto.CoachFitnessPunchListDto;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.imageBaseUrl;
import static com.self.link.base.Constant.RequestUrl.punch;
import static com.self.link.base.Constant.RequsetCode.COURSE_PUNCHLIST_ID;
import static com.self.link.base.Constant.RequsetCode.PUNCH_ID;

public class TuankeModel extends BaseModel {

    public void getPunchData(int pageNum, int pageSize, Location location, OnGetDataListener listener) {

        Map map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);

        if (location != null) {
            map.put("longitude", location.getLongitude());
            map.put("latitude", location.getLatitude());
        } else {
            map.put("longitude", 1);
            map.put("latitude", 1);
        }
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.coursePunchList, COURSE_PUNCHLIST_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == COURSE_PUNCHLIST_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                        boolean hasNextPage = pageObject.hasNextPage;
                        boolean isFirstPage = pageObject.isFirstPage;
                        int pageNum = pageObject.pageNum;
                        List<CoursePunchListDto> punchListDtos = JSONObject.parseArray(pageObject.list, CoursePunchListDto.class);
                        if (punchListDtos != null && punchListDtos.size() > 0) {
                            for (CoursePunchListDto punchListDto : punchListDtos) {
//                            fitnessBody.surfacePlot = imageBaseUrl + fitnessBody.surfacePlot;
                                punchListDto.courseImage = imageBaseUrl + punchListDto.courseImage;

                            }
                            if (listener != null)
                                listener.onComplited(hasNextPage, punchListDtos, isFirstPage, pageNum);
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
                if (id == COURSE_PUNCHLIST_ID) {
                    listener.onfailed(err);
                }
            }
        });

    }

    public interface OnGetDataListener {
        void onComplited(boolean hasNextPage, List<CoursePunchListDto> punchListDtos, boolean isFirstPage, int currentPage);

        void onfailed(String errMsg);
    }
    //打卡
    public void punchCard(String fitnessId, String id, Location location, SijiaoModel.PunchCardListener listener) {
        Map map = new HashMap();
        map.put("fitnessId", fitnessId);
        map.put("id", id);
        map.put("type", 1);

        if (location != null) {
            map.put("longitude", location.getLongitude());
            map.put("latitude", location.getLatitude());
        } else {
            map.put("longitude", 1);
            map.put("latitude", 1);
        }

        netReqModleNew.postJsonHttp(punch, PUNCH_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == PUNCH_ID) {
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
                if (id == PUNCH_ID) {
                    if (listener != null) {
                        listener.onfailed(err);
                    }
                }
            }
        });

    }

    public interface PunchCardListener {
        void oncomplited();

        void onfailed(String err);
    }
}
