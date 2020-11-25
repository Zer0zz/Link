package com.leconssoft.common.NetService.retrofit;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.leconssoft.common.NetService.subscriber.UploadProgressListener;
import com.leconssoft.common.NetService.subscriber.ProgressRequestBody;
import com.leconssoft.common.baseUtils.Constants;
import com.leconssoft.common.baseUtils.SPUtils;
import com.leconssoft.common.baseUtils.baseModle.FileUpReq;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by yucheng on 2017-03-22.
 */

public class RetrofitClient {


    private static final int DEFAULT_TIMEOUT = 10;

    private NetUrlService apiService;

    private OkHttpClient okHttpClient;


    private static RetrofitClient sNewInstance;

    public static RetrofitClient getInstance() {
        if (sNewInstance == null) {
            sNewInstance = new RetrofitClient();
        }
        return sNewInstance;
    }


    private RetrofitClient() {

        if (Constants.isShowLog) {
            okHttpClient = new OkHttpClient.Builder().sslSocketFactory(MySSLSocketFactory.httpGetLeve4()).
                    addInterceptor(MySSLSocketFactory.responseInterceptor).
                    addInterceptor(new NetHeaderInterceptor()).
                    addInterceptor(MySSLSocketFactory.loggingInterceptor).
                    connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                    readTimeout(50, TimeUnit.SECONDS).
                    build();
        } else {
            okHttpClient = new OkHttpClient.Builder().
                    addInterceptor(MySSLSocketFactory.responseInterceptor).
                    addInterceptor(new NetHeaderInterceptor()).
                    connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                    readTimeout(20, TimeUnit.SECONDS).
                    build();
        }
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create(new Gson())).
                addConverterFactory(SimpleXmlConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(Constants.REQUEST_URL_HD).
                build();
        apiService = retrofit.create(NetUrlService.class);
    }

    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable).
                        subscribeOn(rx.schedulers.Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                        .unsubscribeOn(rx.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread());// 指定 Subscriber 的回调发生在主线程
            }
        };
    }


    /**
     * GET 请求 map
     * 无参数
     *
     * @param subscriber
     * @return
     */
    public Subscription getHttp(String url, Subscriber subscriber) {
        return apiService.exGet(url).compose(schedulersTransformer()).subscribe(subscriber);
    }

    /**
     * GET 请求 map 键值对
     *
     * @param parameters 请求实体
     * @param subscriber ResponseBody可以换成对应继承后的子类
     * @return
     */
    public Subscription getMapHttp(String url, Map parameters, Subscriber subscriber) {
        return apiService.executeGet(url, parameters).compose(schedulersTransformer()).subscribe(subscriber);
    }

    /**
     * Post 请求 map 键值对
     *
     * @param url
     * @param parameters
     * @param subscriber
     * @return
     */
    public Subscription postMapHttp(String url, Map parameters, Subscriber subscriber) {
        return apiService.execPost(url, parameters).compose(schedulersTransformer()).subscribe(subscriber);
    }


    /**
     * Post 请求 实体上传
     *
     * @param url
     * @param bean
     * @param subscriber
     * @return
     */
    public Subscription postDataHttp(String url, Object bean, Subscriber subscriber) {
        return apiService.executePostData(url, bean).compose(schedulersTransformer()).subscribe(subscriber);
    }

    /**
     * 多张附件上传
     *
     * @param isOA
     * @param bean
     * @param subscriber
     * @return
     */


    public Subscription postUpFiles(boolean isOA, FileUpReq bean, Subscriber subscriber) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        if (null != bean.getFiles() && bean.getFiles().size() > 0) {
            for (String s : bean.getFiles()) {
                File file = new File(s);
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return apiService.updateFiles(Constants.REQUEST_URL_HD, parts).compose(schedulersTransformer()).subscribe(subscriber);
    }

    /**
     * 单张图片上传
     *
     * @param subscriber
     * @return
     */

    public Subscription postUpFile(FileUpReq req, Subscriber subscriber, UploadProgressListener uploadProgressListener) {
        List<String> fileList = req.getFiles();
        File file = null;
        if (fileList.size() > 0) {
            file = new File(fileList.get(0));//filePath 图片地址
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        if (null != file) {
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/" + type), file);
            builder.addFormDataPart("file", file.getName(), new ProgressRequestBody(imageBody, uploadProgressListener));//imgfile 后台接收图片流的参数名
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return apiService.updateFile(Constants.REQUEST_URL_HD, parts).compose(schedulersTransformer()).subscribe(subscriber);
    }


    public Subscription getDownLoadFile(String url, Subscriber subscriber) {

        return apiService.downLoadFile(url).compose(schedulersTransformer()).subscribe(subscriber);
    }
}
