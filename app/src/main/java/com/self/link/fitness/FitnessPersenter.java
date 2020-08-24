package com.self.link.fitness;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.fitness.body.Attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitnessPersenter extends BasePresenter<FitnessIVeiw> {

    public void getFitnessBanner(String id) {

        Map map = new HashMap();
        map.put("id", id);
        new BaseModel().netReqModleNew.getBandParmHttp(Constant.RequestUrl.fitnessSlideshow, Constant.RequsetCode.FITNESS_SLIDESHOW_ID, map,
                new OnHttpCallBack<String>() {
                    @Override
                    public void onSuccessful(int id, String res) {
                        if (id == Constant.RequsetCode.FITNESS_SLIDESHOW_ID) {
                            BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                            if (baseResponse.isSuccess()) {
                                List<Object> paths =new ArrayList<>();
                                List<Attachment> bannerPics = JSONObject.parseArray(baseResponse.getRe(),Attachment.class);

                                for(int i = 0;i< bannerPics.size();i++){
                                    paths.add(Constant.RequestUrl.imageBaseUrl+bannerPics.get(i).path);
                                }
                                getMvpView().setBannerData(paths);
                            } else {
                                getMvpView().showErrMsg(baseResponse.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFaild(int id, String res, String err) {
                        getMvpView().showErrMsg(err);
                    }
                });
    }

}
