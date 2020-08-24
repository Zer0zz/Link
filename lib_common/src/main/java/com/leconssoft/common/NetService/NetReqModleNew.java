package com.leconssoft.common.NetService;



import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.NetService.retrofit.DownLoadListener;
import com.leconssoft.common.NetService.retrofit.RetrofitClient;
import com.leconssoft.common.NetService.subscriber.BaseNewSubscriber;
import com.leconssoft.common.NetService.subscriber.DownLoadSubscriber;
import com.leconssoft.common.NetService.subscriber.FileNewSubscriber;
import com.leconssoft.common.NetService.subscriber.ObSubscriber;
import com.leconssoft.common.NetService.subscriber.UploadProgressListener;
import com.leconssoft.common.base.BaseApplication;
import com.leconssoft.common.baseUtils.baseModle.FileUpReq;

import java.util.Map;


/**
 * Created by yucheng on 2017-06-05.
 */

public class NetReqModleNew {
    public static  NetReqModleNew sInstance;
    public NetReqModleNew(){
    }
    public static NetReqModleNew getInstance() {
        if (sInstance == null) {
            synchronized (BaseApplication.class) {
                if (sInstance == null) {
                    sInstance = new NetReqModleNew();
                }
            }
        }

        return sInstance;
    }

    public void getHttp(String url,int id,OnHttpCallBack callBack){
        BaseNewSubscriber subscriber = new BaseNewSubscriber(id, callBack);
        RxApiManager.get().add(id, RetrofitClient.getInstance().getHttp(url, subscriber));
    }

    public void getBandParmHttp(String url, int id, Map parameters,OnHttpCallBack callBack){
        BaseNewSubscriber subscriber = new ObSubscriber(id, callBack, "", url);
        RxApiManager.get().add(id, RetrofitClient.getInstance().getMapHttp(url,parameters,subscriber));
    }
    public void getBandParmHttp(int id, Map parameters,OnHttpCallBack callBack){
        BaseNewSubscriber subscriber = new ObSubscriber(id, callBack, "");
        RxApiManager.get().add(id, RetrofitClient.getInstance().getMapHttp("",parameters,subscriber));
    }

    public void postJsonHttp(String url, int id, Object bean, OnHttpCallBack callBack) {
        BaseNewSubscriber subscriber = new ObSubscriber(id, callBack, JSONObject.toJSON(bean), url);
        RxApiManager.get().add(id, RetrofitClient.getInstance().postDataHttp(url, JSONObject.toJSON(bean), subscriber));
    }

    public void postBandParmHttp(String url, int id, Map paramsMap, final OnHttpCallBack callBack) {
        BaseNewSubscriber subscriber = new ObSubscriber(id, callBack,"");
        RxApiManager.get().add(id, RetrofitClient.getInstance().postMapHttp(url, paramsMap, subscriber));
    }


    /**
     * 单张图片上传
     * @param id
     * @param callBack
     */
    public void fileUpLoding(int id, FileUpReq req, OnHttpCallBack callBack, UploadProgressListener uploadProgressListener){
        BaseNewSubscriber subscriber=new FileNewSubscriber(id,callBack,req,false,false);
        RxApiManager.get().add(id,RetrofitClient.getInstance().postUpFile(req,subscriber, uploadProgressListener));
    }

    public  void downLoadFile(int id, String url, String flieName, DownLoadListener loadListener){
        DownLoadSubscriber subscriber=new DownLoadSubscriber(flieName,loadListener);
        RxApiManager.get().add(id,RetrofitClient.getInstance().getDownLoadFile(url,subscriber));

    }

    /**
     * 取消线程订阅
     */
    public void cancel(int id){
        RxApiManager.get().cancel(id);
    }


}
