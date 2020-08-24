package com.leconssoft.common.widgt.adress.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *  政府接口的省份
 * @author yucheng
 * @date 2019/1/9 14:19
 * @Description
 */
public class SfInfo implements Serializable{

    /**
     * sfname : 新疆维吾尔自治区
     * sfcode : 650000000000
     */

    private String sfname;
    private String sfcode;

    public SfInfo() {
    }

    public SfInfo( String sfcode,String sfname) {
        this.sfname = sfname;
        this.sfcode = sfcode;
    }

    public String getSfname() {
        return sfname;
    }

    public void setSfname(String sfname) {
        this.sfname = sfname;
    }

    public String getSfcode() {
        return sfcode;
    }

    public void setSfcode(String sfcode) {
        this.sfcode = sfcode;
    }
}
