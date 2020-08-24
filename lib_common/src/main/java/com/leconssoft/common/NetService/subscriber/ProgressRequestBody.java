package com.leconssoft.common.NetService.subscriber;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by GYH on 2017/6/6.
 */

public class ProgressRequestBody extends RequestBody {
    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final UploadProgressListener listener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, UploadProgressListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        bufferedSink = Okio.buffer(sink(sink));
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();//必须调用flush，否则最后一部分数据可能不会被写入
    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0l;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0l;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writtenBytesCount += byteCount;
                if(totalBytesCount == 0){//获得contentLength的值，后续不再调用
                    totalBytesCount = contentLength();
                }
                Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.onProgress(aLong, totalBytesCount);
                    }
                });
            }
        };
    }
}
