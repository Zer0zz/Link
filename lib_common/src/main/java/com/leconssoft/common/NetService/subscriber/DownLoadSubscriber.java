package com.leconssoft.common.NetService.subscriber;


import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.NetService.retrofit.DownLoadListener;
import com.leconssoft.common.baseUtils.Constants;
import com.leconssoft.common.baseUtils.FileUtils;
import com.leconssoft.common.baseUtils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/12/20 16:02
 * @Description
 */
public class DownLoadSubscriber extends Subscriber<ResponseBody> {
    DownLoadListener listener;
    String fileName;
    private boolean isCancelled;// 是否已取消
    /**
     * 当前已下载字节数
     **/
    private int hasDownloadByteCount = 0;
    /**
     * 尝试建立链接的次数
     **/
    private int tryCount = 0;


    public DownLoadSubscriber(String fileName,DownLoadListener downloadListener){
        this.listener=downloadListener;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("DownLoadSubscriber", e.getMessage());
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        LogUtil.e("DownLoadSubscriber", "执行====onNext");
        File saveFile = FileUtils.createFile(Constants.STORAGE_PATH, fileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            byte[] buffer = new byte[1024];
            inputStream = responseBody.byteStream();  //获取 输入流
            outputStream = new FileOutputStream(saveFile);  //文件的输出流
            long downloadSize = 0;//下载长度
            long contentLength = responseBody.contentLength();  //文件的总长度
            int count = 0;
            while (!isCancelled && (count = inputStream.read(buffer)) > 0) {

                // 如果当前读出的字节数小于已下载的字节数，说明这是重新建立链接，无需更新已下载字节数及百分比
                if (tryCount > 0 && downloadSize < hasDownloadByteCount) {
                    outputStream.write(buffer, 0, count);
                    downloadSize += count;
                    continue;
                }

                outputStream.write(buffer, 0, count);
                downloadSize += count;
                hasDownloadByteCount = (int) downloadSize;
                long progress =
                         ((long) downloadSize * 100 / (long) contentLength);
                listener.onProgress(downloadSize,contentLength,progress);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                listener.onSuccessful(100, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

