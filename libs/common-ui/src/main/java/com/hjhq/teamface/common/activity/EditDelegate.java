package com.hjhq.teamface.common.activity;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * @author Administrator
 * @date 2017/11/9
 * Describe：编辑文本页面
 */

public class EditDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_project_description;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.main_green,"确定");
    }

}
