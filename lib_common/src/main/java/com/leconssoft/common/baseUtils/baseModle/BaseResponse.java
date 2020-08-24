package com.leconssoft.common.baseUtils.baseModle;

import com.alibaba.fastjson.JSON;
import com.leconssoft.common.baseUtils.LogUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by yucheng on 2017-06-01.
 */

public class BaseResponse implements Serializable{
    private BaseResult error;
    private String body;

    public BaseResult getError() {
        return error;
    }

    public void setError(BaseResult error) {
        this.error = error;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public JSONObject toJSON() {
        JSONObject json = null;
        try {
            if (null ==body || body.length() == 0) {
                body = "{}";
                LogUtil.e("ClientResult", "content:" + body);
            }

            if(body.startsWith("[")){
                return new JSONObject(JSON.toJSONString(this));

            }
            if (!body.contains("{")) {
                // 把字符串转为JSON对象
                body = "{ \"" + "content" + "\":\"" + body + "\"}";
                // content = JSON.toJSONString(this.getContent());
            }
            json = new JSONObject(body);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
