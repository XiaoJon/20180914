package com.hjhq.teamface.filelib.applike;


import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by Administrator on 2018/3/26.
 * Describe：
 */

public class FilelibAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("filelib");
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("filelib");
    }
}