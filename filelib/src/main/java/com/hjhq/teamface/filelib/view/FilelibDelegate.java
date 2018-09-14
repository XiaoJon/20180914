package com.hjhq.teamface.filelib.view;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.filelib.R;

/**
 * Created by Administrator on 2018/8/13.
 * Describe：
 */

public class FilelibDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.filelib_main_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("文件库");
    }
}
