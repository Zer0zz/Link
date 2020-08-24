package com.leconssoft.common.update;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/12/5 11:51
 * @Description
 */
public class AppInfo implements Serializable {


    /**
     * id : 5c9a5737e98a458284f464bbab700986
     * bbbh : 1.0.0
     * bbmc : App超级入口V1.0.0
     * apkurl : http://59.175.169.108/app/app.apk
     * bbsm : App超级入口V1.0.0
     * bbfbsj : 2018/12/4 0:00:00
     * bblb : 1
     * sfqzgx : 1
     * isnew : 1
     * tag : 100
     */

    private String id;
    //版本号
    private String bbbh;
    //版本名称
    private String bbmc;
    //app下载地址
    private String apkurl;
    //版本说明
    private String bbsm;
    //版本发布时间
    private String bbfbsj;
    //版本列表
    private String bblb;
    //是否强制更新
    private String sfqzgx;
    //是否有新版本
    private String isnew;
    //tag：标识，100表示最新的版本信息，0历史版本信息
    private String tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBbbh() {
        return bbbh;
    }

    public void setBbbh(String bbbh) {
        this.bbbh = bbbh;
    }

    public String getBbmc() {
        return bbmc;
    }

    public void setBbmc(String bbmc) {
        this.bbmc = bbmc;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getBbsm() {
        return bbsm;
    }

    public void setBbsm(String bbsm) {
        this.bbsm = bbsm;
    }

    public String getBbfbsj() {
        return bbfbsj;
    }

    public void setBbfbsj(String bbfbsj) {
        this.bbfbsj = bbfbsj;
    }

    public String getBblb() {
        return bblb;
    }

    public void setBblb(String bblb) {
        this.bblb = bblb;
    }

    public String getSfqzgx() {
        return sfqzgx;
    }

    public void setSfqzgx(String sfqzgx) {
        this.sfqzgx = sfqzgx;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
