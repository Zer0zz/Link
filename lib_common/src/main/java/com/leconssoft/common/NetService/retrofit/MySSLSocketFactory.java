package com.leconssoft.common.NetService.retrofit;

/**
 * Created by yucheng on 2017/3/8.
 */



import com.leconssoft.common.baseUtils.LogUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class MySSLSocketFactory {

    /**
     * 添加Log打印
     */
    public static HttpLoggingInterceptor
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            LogUtil.e("RetrofitLog", "retrofitBack = " + message);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);


    public static Interceptor responseInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                for (String s : originalResponse.headers("Set-Cookie")) {
                    /**
                     * 设置
                     */
//                    if (s.contains("LOGIN_USERNAME")) {
//                        SPValueUtil.saveStringValue(YunApp.getInstance(),IntentKey.LOGIN_USERNAME,s);
//                    } else if (s.contains("LOGIN_SEQUENCE")) {
//                        SPValueUtil.saveStringValue(YunApp.getInstance(),IntentKey.LOGIN_SEQUENCE,s);
//                    } else if (s.contains("SESSION")) {
//                        SPValueUtil.saveStringValue(YunApp.getInstance(),IntentKey.SESSION,s);
//                    }
                }
            }
            return originalResponse;
        }
    };

    /**
     * 低于4.4的get请求用这个 支持
     * 4.4及以下的机型支持 TLS 1.1和TLS 1.2
     * @return
     */
     public static SSLSocketFactory  httpGetLeve4(){
         SSLContext sslcontext = null;
         try {
             try {
                 sslcontext = SSLContext.getInstance("TLSv1.2");
                 sslcontext.init(null, null, null);
             } catch (NoSuchAlgorithmException e) {
                 e.printStackTrace();
             }
         } catch (KeyManagementException e) {
             e.printStackTrace();
         }
         return new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
    }

    public static OkHttpClient overlockCard() {
        SSLContext sslContext;
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        }};
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .sslSocketFactory(sslContext.getSocketFactory()).build();
            return client;
        } catch (Exception e) {
            LogUtil.e("Https", "ssl出现异常");
        }

        return null;
    }

}
