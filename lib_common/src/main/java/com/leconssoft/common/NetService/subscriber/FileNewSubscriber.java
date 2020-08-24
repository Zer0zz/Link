package com.leconssoft.common.NetService.subscriber;


import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.NetService.retrofit.RetrofitClient;
import com.leconssoft.common.baseUtils.baseModle.FileUpReq;


/**
 * Created by yucheng on 2017-04-24.
 * 图片请求
 */

public class FileNewSubscriber extends BaseNewSubscriber {

    public FileUpReq modelMap;
    public boolean isMuch;//单/多
    public boolean isOA;
    public UploadProgressListener uploadProgressListener;

    public FileNewSubscriber(int id, OnHttpCallBack callBack, FileUpReq map, boolean flag, boolean isOa, UploadProgressListener uploadProgressListener){
        super(id,callBack);
        modelMap=map;
        isMuch=flag;
        isOA=isOa;
        this.uploadProgressListener = uploadProgressListener;
    }

    public FileNewSubscriber(int id, OnHttpCallBack callBack, FileUpReq map,  boolean flag, boolean isOa){
        super(id,callBack);
        modelMap=map;
        isMuch=flag;
        isOA=isOa;
    }

    @Override
    public void restHttp() {
        if(isMuch){
            RetrofitClient.getInstance().postUpFile(modelMap,this, uploadProgressListener);
        }else {
            RetrofitClient.getInstance().postUpFiles(isOA,modelMap,this);
        }
    }
}
