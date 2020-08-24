package com.leconssoft.common.NetService.retrofit;


import com.leconssoft.common.base.BaseResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 当@GET或@POST注解的url为全路径时（可能和baseUrl不是一个域），会直接使用注解的url的域。
 * 如果请求为post实现，那么最好传递参数时使用@Field、@FieldMap和@FormUrlEncoded。
 * 因为@Query和或QueryMap都是将参数拼接在url后面的，而@Field或@FieldMap传递的参数时放在请求体的。
 * 使用@Path时，path对应的路径不能包含”/”，否则会将其转化为%2F。在遇到想动态的拼接多节url时，还是使用@Url吧。
 * Created by yucheng on 2017-03-22.
 * 接口封装类
 */

public interface NetUrlService {

    @GET
    Observable<BaseResponse> exGet(@Url String url);

    /**
     * @param url  请求地址
     * @param maps 请求参数
     * @return
     */
    @GET
    Observable<ResponseBody> executeGet(@Url String url, @QueryMap Map<String, String> maps);


    /**
     * @param url  请求地址
     * @param maps 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> execPost(@Url String url, @FieldMap Map<String, String> maps);

    /**
     * 实体请求
     *
     * @param url
     * @return
     */
    @POST
    Observable<ResponseBody> executePostData(@Url String url, @Body Object baseBean);


    /**
     * 多文件上传
     *
     * @param url
     * @return File file = new File(Environment.getExternalStorageDirectory(), "ic_launcher.png");
     * RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
     * RequestBody descriptionRequestBody = RequestBody.create(null, "this is photo.");
     */

    @Multipart
    @POST
    Observable<ResponseBody> updateFiles(@Url String url, @Part List<MultipartBody.Part> partList);


    /**
     * 单张附件上传
     *
     * @param url
     * @param partList
     * @return
     */
    @Multipart
    @POST
    Observable<ResponseBody> updateFile(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * 附件示例代码
     * File file = new File("/sdcard/Pictures/myPicture/index.jpg");
     * File file1 = new File("/sdcard/Picuures/myPicture/me.txt");
     * <p>
     * RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
     * RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
     * Map<String, RequestBody> params = new HashMap<>();
     * <p>
     * params.put("file\"; filename=\""+ file.getName(), requestBody);
     * params.put("file\"; filename=\""+ file1.getName(), requestBody1);
     * <p>
     * Call<String> model = service.uploadFile(params);
     */

    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@Url String url);

}
