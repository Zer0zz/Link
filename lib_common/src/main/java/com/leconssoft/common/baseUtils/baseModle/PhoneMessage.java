package com.leconssoft.common.baseUtils.baseModle;

import java.io.Serializable;

/**
 * 手机信息
 * Created by yucheng on 2017-10-31.
 */

public class PhoneMessage implements Serializable {

    public String app;// 手机系统android/IOS
    public String os;// 系统版本号
    public String version;// 应用版本号
    public String mobileBrand;// : 手机品牌
    public String model;// 手机型号

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
