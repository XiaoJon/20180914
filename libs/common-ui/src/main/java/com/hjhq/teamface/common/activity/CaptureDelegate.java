package com.hjhq.teamface.common.activity;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.uuzuche.lib_zxing.R;

/**
 * @author Administrator
 * @date 2017/11/9
 * Describe：编辑文本页面
 */

public class CaptureDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.camera;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

}
