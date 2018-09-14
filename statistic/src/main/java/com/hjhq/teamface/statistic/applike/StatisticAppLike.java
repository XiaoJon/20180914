package com.hjhq.teamface.statistic.applike;

import com.hjhq.teamface.componentservice.statistic.StatisticService;
import com.hjhq.teamface.statistic.serviceimpl.StatisticServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by mrzhang on 2017/6/15.
 */
public class StatisticAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();
    Router router = Router.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("statistic");
        router.addService(StatisticService.class.getSimpleName(), new StatisticServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("statistic");
        router.removeService(StatisticService.class.getSimpleName());
    }
}
