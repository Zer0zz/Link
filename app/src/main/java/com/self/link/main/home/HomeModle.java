package com.self.link.main.home;

import android.location.Location;
import android.location.LocationManager;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.main.me.dto.FitnessDto;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequestUrl.imageBaseUrl;
import static com.self.link.base.Constant.RequsetCode.BANNERLISTID;
import static com.self.link.base.Constant.RequsetCode.FITNESSLIST;

/**
 * description：
 * author：Administrator on 2020/5/21 14:28
 */
public class HomeModle extends BaseModel {
    HomePersenter mHomePersenter;
    private boolean hasNextPage = true;

    public HomeModle(HomePersenter homePersenter) {

        this.mHomePersenter = homePersenter;
    }

    public void loadBanerData() {
//        mHomePersenter.getMvpView().showLodingDialog();
        Map map = new ArrayMap();
        map.put("type", "2");
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.bannerList, BANNERLISTID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String respon) {
                if (id == BANNERLISTID) {
                    BaseResponse baseResponse = JSONObject.parseObject(respon, BaseResponse.class);

                    if (baseResponse.isSuccess()) {
                        List<BanerImgBody> banerImgBodys = JSONObject.parseArray(baseResponse.getRe(), BanerImgBody.class);
                        List<Object> listUrls = new ArrayList<>();
                        for (BanerImgBody banerImgBody : banerImgBodys) {
                            listUrls.add(imageBaseUrl + banerImgBody.imgPath);
                        }
                        if (listUrls.size() < 1) {
                            listUrls.add(R.mipmap.banner_pic1);
                            listUrls.add(R.mipmap.banner_pic1);
                        }
                        EventBus.getDefault().post(listUrls);
//                    mHomePersenter.getMvpView().hidLodingDialog();
                    } else {
                        mHomePersenter.getMvpView().showErrMsg(baseResponse.getMsg());
                    }

                }

            }

            @Override
            public void onFaild(int id, String respon, String err) {
//                mHomePersenter.getMvpView().showErrMsg("轮播图片获取失败，失败原因：" + err);
                List<Object> listUrls = new ArrayList<>();
                listUrls.add(R.mipmap.banner_pic1);
                listUrls.add(R.mipmap.banner_pic1);
                EventBus.getDefault().post(listUrls);
                // mHomePersenter.getMvpView().hidLodingDialog();
            }
        });
    }

    public void loadFitnessData(Location location, int pageNum, int pageSize, String vagueQuery) {
//        mHomePersenter.getMvpView().showLodingDialog();
        Map map = new HashMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("applyType", "2");
        if (location != null) {
            map.put("longitude", location.getLongitude());//经度
            map.put("latitude", location.getLatitude());//纬度
        } else {
            map.put("longitude", 1);//经度
            map.put("latitude", 1);//纬度
        }
        if (vagueQuery != null)
            map.put("vagueQuery", vagueQuery);//模糊查询健身房名称/地址
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.fitnessList, FITNESSLIST, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String respon) {
                if (id == FITNESSLIST) {
                    BaseResponse baseResponse = JSONObject.parseObject(respon, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                        hasNextPage = pageObject.hasNextPage;
                        List<FitnessDto> fitnessBodys = JSONObject.parseArray(pageObject.list, FitnessDto.class);
                        for (FitnessDto fitnessBody : fitnessBodys) {
                            fitnessBody.surfacePlot = imageBaseUrl + fitnessBody.surfacePlot;
                        }
                        if (pageObject.pageNum == 1) {
                            mHomePersenter.getMvpView().getPageAdapter().setData(fitnessBodys);
                        } else {
                            mHomePersenter.getMvpView().getPageAdapter().addData(fitnessBodys);
                        }
//                    mHomePersenter.getMvpView().hidLodingDialog();
                    } else {
                        mHomePersenter.getMvpView().showErrMsg(baseResponse.getMsg());
                    }

                }

            }

            @Override
            public void onFaild(int id, String respon, String err) {
                if (id == FITNESSLIST) {
                    mHomePersenter.getMvpView().showErrMsg("获取健身房列表失败，失败原因：" + err);
                    mHomePersenter.getMvpView().hidLodingDialog();
                }
            }
        });

    }

    public boolean hasNextPage() {
        return hasNextPage;
    }


}
