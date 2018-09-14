package com.hjhq.teamface.basis.network.interceptor;

import android.support.annotation.NonNull;

import com.hjhq.teamface.basis.network.exception.ApiException;
import com.hjhq.teamface.basis.network.utils.ZipHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 * Created by jess on 7/1/16.
 */

public class RequestIntercept implements Interceptor {
    private GlobeHttpHandler mHandler;


    public RequestIntercept(GlobeHttpHandler handler) {
        this.mHandler = handler;
    }

    public RequestIntercept() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
        if (mHandler != null) {
            request = mHandler.onHttpRequestBefore(chain, request);
            Socket socket = new Socket();
            DatagramSocket datagramSocket = new DatagramSocket();
        }

        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
            Logger.w("request.body() == null");
        }


        //打印url信息
        String params = request.body() != null ? parseParams(request.body(), requestbuffer) : "无请求参数";
//        Logger.w("Sending Request %s on %n Params --->  %s%n Connection ---> %s%n Headers ---> %s", request.url()
//                , params
//                , chain.connection()
//                , request.headers());

        long t1 = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long t2 = System.nanoTime();
        //打印响应时间
        //Logger.w("Received response  in %.1fms%n%s", (t2 - t1) / 1e6d, originalResponse.headers());
        ResponseBody res = originalResponse.peekBody(1024 * 1024);
        String string = res.string();

        LogUtil.i("\n\n请求方法: " + request.method() +
                "\n连接信息: " + chain.connection() +
                "\n请求头: " + "\n" + request.headers() +
                "\nURL: " + request.url() +
                "\n请求参数: " + params +
                "\n耗时: " + (t2 - t1) / 1e6d + " 毫秒\n ");
        Logger.d("\n返回数据：-------> \n" + string);
        LogUtil.i("<------\n\n ###############################--到此结束--################################\n\n\n");

        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        // Buffer the entire body.
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        int statusCode = originalResponse.code();
        if (statusCode == 500) {
            Logger.e("服务器错误：500");
            throw new ApiException(ApiException.SERVER_ERROR, null);
        }
        //获取content的压缩类型
        String encoding = originalResponse
                .headers()
                .get("Content-Encoding");

        Buffer clone = buffer.clone();
        String bodyString;

        //解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            //content使用gzip压缩
            bodyString = ZipHelper.decompressForGzip(clone.readByteArray());
            //解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {
            //content使用zlib压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());
            //解压
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }

//        Timber.tag("Result").w(jsonFormat(bodyString));
//        Logger.json(bodyString);

        if (mHandler != null)//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
        {
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    @NonNull
    public static String parseParams(RequestBody body, Buffer requestbuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            String content = requestbuffer.readUtf8();
            content = content.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            content = content.replaceAll("\\+", "%2B");
            return URLDecoder.decode(content, "UTF-8");
        }
        return "null";
    }

}
