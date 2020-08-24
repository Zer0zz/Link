package com.leconssoft.common.widgt.adress.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 * 公积金地址 名称
 * @author yucheng
 * @date 2019/1/14 14:41
 * @Description
 */
public class FundInfo implements Serializable{

    public String areaName;
    public String urlPath;

    public FundInfo() {
    }

    public FundInfo(String areaName, String urlPath) {
        this.areaName = areaName;
        this.urlPath = urlPath;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
}
