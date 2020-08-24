package com.leconssoft.common.NetService.subscriber;


import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.NetService.retrofit.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by yucheng on 2017-06-06.
 * 数据请求
 */

public class ObSubscriber extends BaseNewSubscriber<ResponseBody> {
    public Object modelMap;
    public String rUrl;

    public ObSubscriber(int id, OnHttpCallBack callBack, Object map, String url){
        super(id,callBack);
        modelMap= map;
        rUrl=url;
    }

    public ObSubscriber(int id, OnHttpCallBack callBack, Object map){
        super(id,callBack);
        modelMap= map;
    }

    @Override
    public void onNext(ResponseBody body) {
        if(body!=null){
            String jsonString = null;
            try {
                jsonString = body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callBack.onSuccessful(id,jsonString);
        }else {
            onException(HttpExceptionReason.LODING_ERROR);
        }
    }

    @Override
    public void restHttp() {
    }

    public void restLogin(){
        /**
         * 这里做一个重新登陆 或者 重新请求的方法
         */
    }
}
