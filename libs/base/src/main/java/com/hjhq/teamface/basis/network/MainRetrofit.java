/*
 *
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 * Copyright (C) 2015 GuDong <maoruibin9035@gmail.com>
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hjhq.teamface.basis.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.interceptor.AppInterceptor;
import com.hjhq.teamface.basis.network.interceptor.NetworkInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * Retrofit初始化配置
 *
 * @author Administrator
 */
public class MainRetrofit<T> {
    //默认连接时长
    public static final int DEFAULT_CONNECT_TIMEOUT = 10;
    //默认读取时长
    public static final int DEFAULT_READ_TIMEOUT = 30;
    //默认写入时长
    public static final int DEFAULT_WRITE_TIMEOUT = 30;

    /**
     * json格式工具
     */
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
    /**
     * 连接超时时间
     */
    private int connectTimeout;
    /**
     * 读取超时时间
     */
    private int readTimeout;
    /**
     * 写入超时时间
     */
    private int writeTimeout;
    /**
     * 网络拦截器
     */
    private Interceptor networkInterceptor;
    /**
     * 日志拦截器
     */
    private Interceptor appInterceptor;
    /**
     * 自动重连
     */
    private boolean retryOnConnectionFailure;
    /**
     * 缓存
     */
    private Cache cache;
    /**
     * 主机ip
     */
    private String baseUrl;
    /**
     * 调用适配器
     */
    private CallAdapter.Factory callAdapterfactory;
    /**
     * 格式转换器
     */
    private Converter.Factory converterfactory;
    private OkHttpClient client;
    private Retrofit retrofit;

    private MainRetrofit(Builder builder) {
        connectTimeout = builder.connectTimeout;
        readTimeout = builder.readTimeout;
        writeTimeout = builder.writeTimeout;
        networkInterceptor = builder.networkInterceptor;
        appInterceptor = builder.appInterceptor;
        retryOnConnectionFailure = builder.retryOnConnectionFailure;
        cache = builder.cache;
        baseUrl = builder.baseUrl;
        callAdapterfactory = builder.callAdapterfactory;
        converterfactory = builder.converterfactory;
    }

    public T getApi(Class<T> api) {
        client = new OkHttpClient.Builder()
                //断开重连
                .retryOnConnectionFailure(retryOnConnectionFailure)
                //读取时间
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                //连接时间
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                //写入时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                //自定义拦截器
                .addInterceptor(appInterceptor)
                //网络拦截
                .addNetworkInterceptor(networkInterceptor)
                //缓存
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                //增加 RxJava 适配器
                .addCallAdapterFactory(callAdapterfactory)
                .addConverterFactory(converterfactory)
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(api);
    }

    public static final class Builder<T> {
        /**
         * 连接超时时间
         */
        int connectTimeout;
        /**
         * 读取超时时间
         */
        int readTimeout;
        /**
         * 写入超时时间
         */
        int writeTimeout;
        /**
         * 网络拦截器
         */
        Interceptor networkInterceptor;
        /**
         * 自定义拦截器
         */
        Interceptor appInterceptor;
        /**
         * 自动重连
         */
        boolean retryOnConnectionFailure;
        /**
         * 缓存
         */
        Cache cache;
        /**
         * 主机ip
         */
        String baseUrl;
        /**
         * 默认Rx转换器
         */
        CallAdapter.Factory callAdapterfactory;
        /**
         * 格式转默认Gson转换器换器
         */
        Converter.Factory converterfactory;

        public Builder() {
            connectTimeout = DEFAULT_CONNECT_TIMEOUT;
            readTimeout = DEFAULT_READ_TIMEOUT;
            writeTimeout = DEFAULT_WRITE_TIMEOUT;
            appInterceptor = new AppInterceptor.Builder().build();
            networkInterceptor = new NetworkInterceptor.Builder().build();
            retryOnConnectionFailure = true;
            baseUrl = Constants.BASE_URL;
            callAdapterfactory = RxJavaCallAdapterFactory.create();
            converterfactory = GsonConverterFactory.create(gson);
        }

        public T build(Class<T> api) {
            return new MainRetrofit<T>(this).getApi(api);
        }

        public MainRetrofit build() {
            return new MainRetrofit(this);
        }

        public Builder<T> retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder<T> readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder<T> connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder<T> writeTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder<T> addAppInterceptor(Interceptor appInterceptor) {
            this.appInterceptor = appInterceptor;
            return this;
        }

        public Builder<T> addNetworkInterceptor(Interceptor interceptor) {
            this.networkInterceptor = interceptor;
            return this;
        }

        public Builder<T> cache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder<T> baseUrl(String baseUrl) {
            this.baseUrl = checkNotNull(baseUrl);
            return this;
        }

        public Builder<T> addCallAdapterFactory(CallAdapter.Factory factory) {
            this.callAdapterfactory = checkNotNull(factory);
            return this;
        }

        public Builder<T> addConverterFactory(Converter.Factory factory) {
            this.converterfactory = checkNotNull(factory);
            return this;
        }
    }
}
