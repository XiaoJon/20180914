package com.hjhq.teamface.common.activity;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * Created by Administrator on 2018/8/13.
 * Describe：
 */

public class ViewImageDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_imagepage;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }
}
