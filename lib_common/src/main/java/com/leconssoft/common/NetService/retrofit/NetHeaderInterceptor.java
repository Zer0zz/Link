package com.leconssoft.common.NetService.retrofit;

import android.text.TextUtils;

import com.leconssoft.common.baseUtils.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置请求头
 * Created by yucheng on 2017-03-22.
 */

public class NetHeaderInterceptor implements Interceptor {
    private Map<String, String> headers;

    /**
     * 根据需要配置
     */
    public NetHeaderInterceptor() {
        Map<String, String> headerMap = new HashMap<String, String>();

        this.headers = headerMap;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        String sessionId = SPUtils.getStringValue("sessionId");
        if (!TextUtils.isEmpty(sessionId)) {
            headers.put("sessionId", sessionId);
        }
//        Response response = chain.proceed(builder.build());
//        BaseResponse baseResponse = JSONObject.parseObject(response.body().string(), BaseResponse.class);
//        if (baseResponse.isSuccess()) {
//            LoginRe loginRe = JSONObject.parseObject(baseResponse.getRe(), LoginRe.class);
//            if (loginRe != null) {
//                if (loginRe.getSessionId() != null)
//                    headers.put("sessionId", loginRe.getSessionId());
//            }
//        }
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();

            }
        }
        return chain.proceed(builder.build());
    }

    public Response intercepts(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());
    }

}
