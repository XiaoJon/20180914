package com.hjhq.teamface.filelib.view;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.filelib.R;

/**
 * Created by Administrator on 2018/8/13.
 * Describe：
 */

public class FileMainDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.filelib_main_file_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuIcons(R.drawable.plus_icon_gray);
        showMenu();
    }
}
