package com.leconssoft.common.NetService.subscriber;

/**
 * 文件上传进度监听
 * Created by GYH on 2017/6/6.
 */

public interface UploadProgressListener {
    /**
     *上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
