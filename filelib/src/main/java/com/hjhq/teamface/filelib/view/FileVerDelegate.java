package com.hjhq.teamface.filelib.view;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.filelib.R;

/**
 * Created by Administrator on 2018/8/13.
 * Describe：
 */

public class FileVerDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.filelib_download_log_activity;
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
