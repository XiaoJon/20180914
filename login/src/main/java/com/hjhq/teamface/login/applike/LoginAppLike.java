package com.hjhq.teamface.login.applike;

import com.hjhq.teamface.componentservice.login.LoginService;
import com.hjhq.teamface.login.serviceimpl.LoginServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by mrzhang on 2017/6/15.
 */
public class LoginAppLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("login");
        router.addService(LoginService.class.getSimpleName(), new LoginServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("login");
        router.removeService(LoginService.class.getSimpleName());
    }
}
