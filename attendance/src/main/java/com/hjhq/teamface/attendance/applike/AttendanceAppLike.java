package com.hjhq.teamface.attendance.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * 注册考勤模块到路由
 */
public class AttendanceAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("attendance");

    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("attendance");

    }
}
