package com.leconssoft.common.widgt.adress.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2019/1/9 14:27
 * @Description
 */
public class QxInfo implements Serializable{

    /**
     * qxname : 香港特别行政区
     * qxcode : 810000000000
     */

    private String qxname;
    private String qxcode;

    public QxInfo() {
    }

    public QxInfo( String qxcode,String qxname) {
        this.qxname = qxname;
        this.qxcode = qxcode;
    }

    public String getQxname() {
        return qxname;
    }

    public void setQxname(String qxname) {
        this.qxname = qxname;
    }

    public String getQxcode() {
        return qxcode;
    }

    public void setQxcode(String qxcode) {
        this.qxcode = qxcode;
    }
}
