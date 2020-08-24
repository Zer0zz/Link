package com.leconssoft.common.baseUtils.baseModle;

/**
 * Created by yucheng on 2017-06-07.
 */

public class BaseResult {

    /**
     * code : 14
     * content : 帐号密码错误
     */

    public int result;
    public String resultRemark;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultRemark() {
        return resultRemark;
    }

    public void setResultRemark(String resultRemark) {
        this.resultRemark = resultRemark;
    }
}
