package com.self.link.fitness.coach;

import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.self.link.base.Constant;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.COACHLISTID;

public class CoachModel extends BaseModel {
    private int pageSize = 100;


    public void getCoachData(String fitnessId, int pageNum, OnHttpCallBack callBack) {
        OnHttpCallBack<String> mOnHttpCallBack;
        if (callBack == null) {
            mOnHttpCallBack = new OnHttpCallBack<String>() {
                @Override
                public void onSuccessful(int id, String res) {

                }

                @Override
                public void onFaild(int id, String res, String err) {

                }
            };
        } else {
            mOnHttpCallBack = callBack;
        }

        Map para = new HashMap();
        para.put("pageNum", pageNum);
        para.put("pageSize", pageSize);
        para.put("fitnessId", fitnessId);
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.coachList, COACHLISTID, para, mOnHttpCallBack);
    }
}
