package com.hjhq.teamface.basis.network.subscribers;

import android.content.Context;
import android.text.TextUtils;

import com.hjhq.teamface.basis.view.load.LoadingDialog;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 *
 * @author Administrator
 */
public class ProgressSubscriber<T> extends BaseSubscriber<T> implements ProgressCancelListener {

    private boolean isShow = true;
    /**
     * 1 onstart 时显示loadding
     */
    public int type;
    private String content = "请稍等...";
    private LoadingDialog loadingDialog;

    public ProgressSubscriber(Context context) {
        super(context);
        showWindowView();
    }

    public ProgressSubscriber(Context context, boolean isShow) {
        super(context);
        this.isShow = isShow;
        showWindowView();
    }

    public ProgressSubscriber(Context context, String content) {
        super(context);
        if (!TextUtils.isEmpty(content)) {
            this.content = content;
        }
        showWindowView();
    }

    public ProgressSubscriber(Context context, int type) {
        super(context);
        this.type = type;
    }

    private void showWindowView() {
        if (isShow) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.setLoadingText(content);
            loadingDialog.setInterceptBack(false);
            loadingDialog.show();
//            ProgressToast.getInstance().createWindowView(context, content);
//            context.startActivity(new Intent(context, DialogProgressActivity.class));
        }
    }

    public void dismissWindowView() {
        if (isShow) {
//            ProgressToast.getInstance().closeWindowView();
//            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.PROGRESS_ACTION_CLOSE));
            loadingDialog.close();
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (type == 1) {
            showWindowView();
        }
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
        super.onError(e);
        dismissWindowView();
        e.printStackTrace();
        showErrorToast(e);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        dismissWindowView();
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        dismissWindowView();
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}