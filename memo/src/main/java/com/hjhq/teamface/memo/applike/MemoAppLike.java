package com.hjhq.teamface.memo.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Created by Administrator on 2018/3/26.
 * Describe：
 */

public class MemoAppLike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("memo");
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("memo");
    }
}