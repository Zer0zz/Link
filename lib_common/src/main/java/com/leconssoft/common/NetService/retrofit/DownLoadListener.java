package com.leconssoft.common.NetService.retrofit;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/12/20 16:49
 * @Description
 */
public interface DownLoadListener<T> {
    void onSuccessful(int id, T t);//成功了就回调这个方法,可以传递任何形式的数据给调用者

    void onFaild(int id, T t, String err);//失败了就调用这个方法,可以传递错误的请求消息给调用者

    void onProgress(long...value);
}
