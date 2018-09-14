package com.hjhq.teamface.common.activity;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * Created by Administrator on 2018/8/13.
 * Describe：
 */

public class WebviewDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.webview_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }
}
