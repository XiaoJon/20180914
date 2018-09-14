package com.hjhq.teamface.login.application;


import android.app.Application;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class LoginApplication extends Application {
    private LoginApplication context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        EventBusUtils.register(context);
        SPHelper.init(context);
        AppManager.init(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        int code = event.getCode();
        switch (code) {
            case EventConstant.LOGIN_TAG:
                ToastUtils.showSuccess(context,"登录成功");
                break;
            default:
                break;
        }
    }
}
