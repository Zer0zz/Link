package com.leconssoft.common.BaseMvp.model;


import com.leconssoft.common.NetService.NetReqModleNew;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述：
 */
public class BaseModel {
    public NetReqModleNew netReqModleNew = NetReqModleNew.getInstance();

    public void cancel(int id) {
        netReqModleNew.cancel(id);
    }

}
