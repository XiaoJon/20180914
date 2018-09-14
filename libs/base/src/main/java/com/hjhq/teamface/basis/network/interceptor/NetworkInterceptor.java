package com.hjhq.teamface.basis.network.interceptor;

import android.support.annotation.NonNull;

import com.hjhq.teamface.basis.network.utils.ZipHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 网络拦截器
 *
 * @author Jun
 * @date 2017/2/9
 */

public class NetworkInterceptor implements Interceptor {
    private Builder mBuilder;


    private NetworkInterceptor(Builder builder) {
        this.mBuilder = builder;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
        if (mBuilder != null) {
            request = mBuilder.onHttpRequestBefore(chain, request);
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
        Logger.d("\n返回数据：------->" + string);
        LogUtil.i("<------\n\n ###############################--到此结束--################################\n\n\n");

        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        // Buffer the entire body.
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

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
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {
            //content使用zlib压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }

        if (mBuilder != null) {
            //这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mBuilder.onHttpResultResponse(bodyString, chain, originalResponse);
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

    public static class Builder {
        static final String NO = "no";
        static final String TOKEN_TYPE = "Token-Type";
        GlobeHttpHandler handler;

        Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            if (handler == null) {
                return response;
            } else {
                return handler.onHttpResultResponse(httpResult, chain, response);
            }
        }

        Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            if (handler == null) {
                if (NO.equals(request.header(TOKEN_TYPE))) {
                    return request;
                }
                //加上统一的请求头
                request = request.newBuilder()
                        //强制使用网络
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .method(request.method(), request.body())
                        .build();
                return request;
            } else {
                return handler.onHttpRequestBefore(chain, request);
            }
        }

        public void setHandler(GlobeHttpHandler handler) {
            this.handler = handler;
        }

        interface GlobeHttpHandler {
            Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

            Request onHttpRequestBefore(Interceptor.Chain chain, Request request);
        }

        public NetworkInterceptor build() {
            return new NetworkInterceptor(this);
        }
    }
}
