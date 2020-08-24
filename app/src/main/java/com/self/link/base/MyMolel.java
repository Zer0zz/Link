package com.self.link.base;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import static com.self.link.base.Constant.RequsetCode.CERTIFICATION_STATUS_ID;

public class MyMolel extends BaseModel {
    //状态：1.未认证 2.已过期 3.已认证 4.审核中
    public void certificationStatus(String fitnessId, GetCertificationStatusListener listener) {
        Map map = new HashMap();
        map.put("fitnessId", fitnessId);
        netReqModleNew.getBandParmHttp(Constant.RequestUrl.certificationStatus, CERTIFICATION_STATUS_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (id == CERTIFICATION_STATUS_ID) {
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {//成功
                        Status mStatus = JSONObject.parseObject(baseResponse.getRe(), Status.class);
                        int status = mStatus.status;
                        if (listener != null) {
                            listener.onSuccess(status);
                        }
                    } else {//失败
                        if (listener != null) {
                            listener.onFailed(baseResponse.getMsg());
                        }
                    }
                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (id == CERTIFICATION_STATUS_ID) {
                    if (listener != null) {
                        listener.onFailed(err);
                    }
                }

            }
        });
    }

    public interface GetCertificationStatusListener {
        void onSuccess(int satus);

        void onFailed(String errMsg);
    }

    public class Status {
        public int status;
    }
}
