package com.hjhq.teamface.basis.network.subscribers;

import android.content.Context;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.network.exception.ApiException;
import com.hjhq.teamface.basis.util.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    protected Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {

    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (!(e instanceof ApiException)) {
            e.printStackTrace();
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {

    }

    protected void showErrorToast(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            ToastUtils.showError(context, context.getString(R.string.network_disconnect));
        } else if (e instanceof ConnectException) {
            ToastUtils.showError(context, context.getString(R.string.connect_server_timeout));
        } else if (e instanceof ApiException) {
            ToastUtils.showError(context, e.getMessage());
        } else {
            ToastUtils.showError(context, context.getString(R.string.data_error));
        }
    }

}