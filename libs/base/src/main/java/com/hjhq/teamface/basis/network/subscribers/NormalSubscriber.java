package com.hjhq.teamface.basis.network.subscribers;

import android.content.Context;

/**
 * 调用者自己对请求数据进行处理
 */
public class NormalSubscriber<T> extends BaseSubscriber<T> implements NomalCancelListener {
    

    public NormalSubscriber(Context context) {
     super(context);   
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

//        Toast.makeText(context, "request Completed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        super.onError(e);
        showErrorToast(e);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {

    }


    @Override
    public void onCancelRequest() {

    }


   
}
