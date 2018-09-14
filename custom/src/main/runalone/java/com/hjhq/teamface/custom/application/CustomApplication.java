package com.hjhq.teamface.custom.application;

import android.app.Application;

import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 *
 * @author Administrator
 * @date 2018/3/13
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册路由
        UIRouter.getInstance().registerUI("custom");
    }

}
