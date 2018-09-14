package com.hjhq.teamface.email.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by Administrator on 2018/3/26.
 * Describe：
 */

public class EmailAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("email");
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("email");
    }
}