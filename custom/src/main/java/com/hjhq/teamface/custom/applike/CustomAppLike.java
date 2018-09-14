package com.hjhq.teamface.custom.applike;

import com.hjhq.teamface.componentservice.custom.CustomService;
import com.hjhq.teamface.custom.serviceimpl.CustomServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by mrzhang on 2017/6/15.
 */
public class CustomAppLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("custom");
        router.addService(CustomService.class.getSimpleName(), new CustomServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("custom");
        router.removeService(CustomService.class.getSimpleName());
    }
}
