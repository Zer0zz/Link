package com.leconssoft.common.widgt.adress.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 * 政府地市区
 * @author yucheng
 * @date 2019/1/9 14:24
 * @Description
 */
public class DszInfo implements Serializable{


    /**
     * dszname : 黄石市
     * dszcode : 420200000000
     */

    private String dszname;
    private String dszcode;

    public DszInfo() {
    }

    public DszInfo(String dszcode,String dszname ) {
        this.dszname = dszname;
        this.dszcode = dszcode;
    }

    public String getDszname() {
        return dszname;
    }

    public void setDszname(String dszname) {
        this.dszname = dszname;
    }

    public String getDszcode() {
        return dszcode;
    }

    public void setDszcode(String dszcode) {
        this.dszcode = dszcode;
    }
}
