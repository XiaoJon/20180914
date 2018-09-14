package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.network.callback.DownloadCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public final class FileRequestBody<T> extends RequestBody {
    /**
     * 实际请求体
     */
    private RequestBody requestBody;
    private BufferedSink mBufferedSink;
    /**
     * 上传回调接口
     */
    private DownloadCallback<T> callback;
    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;

    public FileRequestBody(RequestBody requestBody, DownloadCallback callback) {
        super();
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /*@Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }*/

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            // Log Interceptor
            requestBody.writeTo(sink);
            return;
        }
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    /*private Sink wrapSink(Sink sink) {
        return new ForwardingSink(sink) {

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (mProgressInfo.total == 0) {
                    mProgressInfo.total = contentLength();
                }
                mProgressInfo.current += byteCount;
                mEmitter.onNext(mProgressInfo);
            }
        };
    }*/

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                callback.onLoading(contentLength, bytesWritten);
            }
        };
    }
}
