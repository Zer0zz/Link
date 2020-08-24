package com.self.link.selectseat;

import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.self.link.base.Constant;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.COURSESEAT_LIST_ID;

/**
 * description：
 * author：Administrator on 2020/5/30 22:14
 */
public class SelectSeatModel extends BaseModel {


    public SelectSeatModel() {

    }

    public void loadData(String courseId, OnHttpCallBack callBack) {
        Map map = new HashMap();
        map.put("courseId", courseId);
        if (callBack == null) {
            callBack = new OnHttpCallBack<String>() {
                @Override
                public void onSuccessful(int id, String res) {

                }

                @Override
                public void onFaild(int id, String res, String err) {

                }
            };
        }
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.courseSeatList, COURSESEAT_LIST_ID, map, callBack);
    }

}
