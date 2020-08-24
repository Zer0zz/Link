package com.self.link.main.me;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.baseUtils.SPUtils;
import com.leconssoft.common.baseUtils.baseModle.BaseResult;
import com.self.link.base.Constant;
import com.self.link.base.PageObject;
import com.self.link.base.UserInfo;
import com.self.link.main.me.dto.FitnessDto;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.MYFITNESS_ID;
import static com.self.link.base.Constant.RequsetCode.USERINFO;
import static com.self.link.base.Constant.SpKeyName.phone;
import static com.self.link.base.Constant.SpKeyName.realName;
import static com.self.link.base.Constant.SpKeyName.userId;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/9/28 10:04
 * @Description
 */
public class MePresenter extends BasePresenter<MeIView> {
    public final static int pageSize = 5;
    private static final String TAG = "MePresenter";
    //在presenter中引用 model
    MeMvpModel meMvpModel = new MeMvpModel(this);

    boolean isLoading = true;
    private boolean hasNextPage = true;

    public void getUserInfo(int pageNum, int pageSize) {
        getMvpView().showLodingDialog();
        Map map1 = new HashMap();
        meMvpModel.netReqModleNew.getBandParmHttp(Constant.RequestUrl.userInfo, USERINFO, map1, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String response) {
                if (id == USERINFO) {
                    BaseResponse baseResponse = JSONObject.parseObject(response, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        UserInfo userInfo = JSONObject.parseObject(baseResponse.getRe(), UserInfo.class);
                        SPUtils.saveStringValue(userId, userInfo.id);
                        SPUtils.saveStringValue(realName, userInfo.realName);
                        SPUtils.saveStringValue(phone, userInfo.phone);
                        // 发送黏性消息
                        if (userInfo != null)
                            EventBus.getDefault().postSticky(userInfo);
                        if (!isLoading) {
                            getMvpView().hidLodingDialog();
                        }
                    }
                    isLoading = false;
                }
            }

            @Override
            public void onFaild(int id, String responseBody, String err) {
                if (id == USERINFO) {
                    getMvpView().showErrMsg(err);
                    Log.e("MeModel", "err:" + err);
                    if (!isLoading) {
                        getMvpView().hidLodingDialog();
                    }
                    isLoading = false;
                }

            }
        });


        Map map2 = new HashMap();
        map2.put("pageNum", pageNum);
        map2.put("pageSize", pageSize);
        meMvpModel.netReqModleNew.getBandParmHttp(Constant.RequestUrl.myFitness, MYFITNESS_ID, map2, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == MYFITNESS_ID) {
                    Log.e("tag", res);
                    if (!isLoading) {
                        getMvpView().hidLodingDialog();
                    }
                    isLoading = false;
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        PageObject pageObject = JSONObject.parseObject(baseResponse.getRe(), PageObject.class);
                        hasNextPage = pageObject.hasNextPage;
                        List<FitnessDto> fitnessDtos = JSONObject.parseArray(pageObject.list, FitnessDto.class);
                        if (pageNum == 1) {//刷新
                            getMvpView().getAdapter().setNewData(fitnessDtos);
                            getMvpView().setRefreshing(false);
                            getMvpView().getAdapter().setEnableLoadMore(true);
                        } else {//加载更多
                            getMvpView().getAdapter().addData(fitnessDtos);
                        }

                        getMvpView().getAdapter().notifyDataSetChanged();
                        getMvpView().getAdapter().loadMoreComplete();

                    }
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == MYFITNESS_ID) {
                    Log.e("tag_err", "res:" + res + "err:" + err);
                    getMvpView().showErrMsg(err);
                    if (!isLoading) {
                        getMvpView().hidLodingDialog();
                    }
                    isLoading = false;
                }

            }
        });
    }

    public boolean hasNextPage() {

        return hasNextPage;
    }

}
