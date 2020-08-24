package com.self.link.daka.sijiao;

import android.location.Location;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.daka.sijiao.dto.CoachFitnessPunchListDto;
import com.self.link.mycenter.dto.PersonalCourseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.imageBaseUrl;
import static com.self.link.base.Constant.RequestUrl.punch;
import static com.self.link.base.Constant.RequsetCode.COACHFITNESS_PUNCHLIST_ID;
import static com.self.link.base.Constant.RequsetCode.PUNCH_ID;

public class SijiaoModel extends BaseModel {


    public void getSijiaoData(int pageNum, int pageSize, Location location, OnGetDataListener listener) {
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

        netReqModleNew.getBandParmHttp(Constant.RequestUrl.coachFitnessPunchList, COACHFITNESS_PUNCHLIST_ID
                , map, new OnHttpCallBack<String>() {
                    @Override
                    public void onSuccessful(int id, String res) {

                        if (id == COACHFITNESS_PUNCHLIST_ID) {
                            BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                            if (baseResponse.isSuccess()) {
                                PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                                boolean hasNextPage = pageObject.hasNextPage;
                                boolean isFirstPage = pageObject.isFirstPage;
                                int pageNum = pageObject.pageNum;
                                List<CoachFitnessPunchListDto> punchListDtos = JSONObject.parseArray(pageObject.list, CoachFitnessPunchListDto.class);

                                if (punchListDtos != null && punchListDtos.size() > 0) {
                                    for (CoachFitnessPunchListDto punchListDto : punchListDtos) {

                                        punchListDto.headPortrait = imageBaseUrl + punchListDto.headPortrait;
                                        punchListDto.personalImage = imageBaseUrl + punchListDto.personalImage;

                                    }
                                    if (listener != null)
                                        listener.onComplited(hasNextPage, punchListDtos, isFirstPage, pageNum);
                                }
                            } else {
                                if (listener != null)
                                    listener.onfailed(baseResponse.getMsg());
                            }

                        }
                    }

                    @Override
                    public void onFaild(int id, String res, String err) {
                        if (id == COACHFITNESS_PUNCHLIST_ID) {
                            if (listener != null)
                                listener.onfailed(err);
                        }
                    }
                });
    }


    public interface OnGetDataListener {
        void onComplited(boolean hasNextPage, List<CoachFitnessPunchListDto> punchListDtos, boolean isFirstPage, int currentPage);

        void onfailed(String errMsg);
    }

    //打卡
    public void punchCard(String fitnessId, String id, Location location, PunchCardListener listener) {
        Map map = new HashMap();
        map.put("fitnessId", fitnessId);
        map.put("id", id);
        map.put("type", 2);

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
