package com.self.link.coach;

import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.self.link.base.Constant;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.COACHDETAIL_ID;

/**
 * description：
 * author：Administrator on 2020/5/31 21:23
 */
public class CoachModel extends BaseModel {

    public void getCoachDetail(String coachId, String startDate, String endDate, OnHttpCallBack callBack) {

        Map map = new HashMap();
        map.put("coachId", coachId);
        if (startDate != null)
            map.put("startDate", startDate);
        if (endDate != null)
            map.put("endDate", endDate);
        if (callBack == null) {
            return;
        }
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.coachDetail, COACHDETAIL_ID, map, callBack);
    }
}
