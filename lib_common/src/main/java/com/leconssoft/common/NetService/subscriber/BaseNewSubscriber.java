package com.leconssoft.common.NetService.subscriber;


import android.util.Log;

import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.baseUtils.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by yucheng on 2017-06-02.
 * Retrofit
 * 请求处理抽象类 这里面可以对网络处理信息做一系列的处理
 * 可以定义泛型完成数据解析操作
 */

public class BaseNewSubscriber<T> extends Subscriber<T> {
    OnHttpCallBack callBack;
    int id;

    /**
     * @param id
     * @param callBack
     */
    public BaseNewSubscriber(int id, OnHttpCallBack callBack) {
        this.callBack = callBack;
        this.id = id;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            Log.d("HTTPERR","code = "+code);
            if (code == 500 || code == 404) {
                onException(HttpExceptionReason.SERVE_ERROR);
            } else if (code == 405) {
                onException(HttpExceptionReason.CONNECT_ERROR);
            }
        } else if (e instanceof ConnectException) {
            onException(HttpExceptionReason.BAD_NETWORK);
        } else if (e instanceof SocketTimeoutException) {
            onException(HttpExceptionReason.CONNECT_TIMEOUT);
        } else {
            onException(HttpExceptionReason.UNKNOWN_ERROR);
            LogUtil.e("MyLog", e.getMessage());
        }
    }


    public void onException(HttpExceptionReason reason) {
        String errMsg = "服务器异常";
        switch (reason) {
            case BAD_NETWORK:
                errMsg = "网络异常!!";
                break;
            case PARSE_ERROR:
                errMsg = "数据解析失败!!";
                break;
            case CONNECT_ERROR:
                errMsg = "请求配置出错!!";
                break;
            case CONNECT_TIMEOUT:
                errMsg = "网络连接超时!!";
                break;
            case UNKNOWN_ERROR:
                errMsg = "数据获取失败";
                break;
            case LODING_ERROR:
                errMsg = "访问错误";
                break;
        }
        callBack.onFaild(id, null, errMsg);

    }

    /**
     * @param body
     */
    @Override
    public void onNext(T body) {
        if (body != null) {
            callBack.onSuccessful(id, body);
        } else {
            onException(HttpExceptionReason.LODING_ERROR);
        }
    }

    /**
     * 重新请求方法
     */
    public void restHttp() {
    }
}
