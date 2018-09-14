package com.hjhq.teamface.project.applike;

import com.hjhq.teamface.componentservice.project.ProjectService;
import com.hjhq.teamface.project.serviceimpl.ProjectServiceImpl;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by mrzhang on 2017/6/15.
 */
public class ProjectAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();
    Router router = Router.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("project");
        router.addService(ProjectService.class.getSimpleName(), new ProjectServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("project");
        router.removeService(ProjectService.class.getSimpleName());
    }
}
