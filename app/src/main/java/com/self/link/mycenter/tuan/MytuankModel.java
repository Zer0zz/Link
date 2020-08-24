package com.self.link.mycenter.tuan;

import android.location.Location;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;
import com.self.link.mycenter.dto.PersonalCourseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.imageBaseUrl;
import static com.self.link.base.Constant.RequsetCode.MYCOURSE_ID;

public class MytuankModel extends BaseModel {

    public void getMytuankeData(int pageNum, int pageSize, String fitnessId, Location location,OnGetMyCourseDataListener listener) {

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

        netReqModleNew.getBandParmHttp(Constant.RequestUrl.myCourse, MYCOURSE_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == MYCOURSE_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                        boolean hasNextPage = pageObject.hasNextPage;
                        boolean isFirstPage = pageObject.isFirstPage;
                        int pageNum = pageObject.pageNum;

                        List<CoursePunchListDto> coursePunchListDtos = JSONObject.parseArray(pageObject.list, CoursePunchListDto.class);

                        if (coursePunchListDtos != null && coursePunchListDtos.size() > 0) {
                            for (CoursePunchListDto coursePunchListDto : coursePunchListDtos) {
                                coursePunchListDto.courseImage = imageBaseUrl + coursePunchListDto.courseImage;
                            }
                            if (listener != null)
                                listener.onComplited(hasNextPage, coursePunchListDtos, isFirstPage, pageNum);
                        }
                    } else {
                        if (listener != null)
                            listener.onfailed(baseResponse.getMsg());
                    }
                }
            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == MYCOURSE_ID) {
                    if (listener != null)
                        listener.onfailed(err);
                }
            }
        });

    }

    public interface OnGetMyCourseDataListener {
        void onComplited(boolean hasNextPage, List<CoursePunchListDto> coursePunchDtos, boolean isFirstPage, int currentPage);

        void onfailed(String errMsg);
    }
}
