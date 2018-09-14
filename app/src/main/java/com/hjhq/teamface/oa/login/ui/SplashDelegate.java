package com.hjhq.teamface.oa.login.ui;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * 启动界面
 */

public class SplashDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }
}
