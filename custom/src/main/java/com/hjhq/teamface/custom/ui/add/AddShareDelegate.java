package com.hjhq.teamface.custom.ui.add;

import android.support.v7.widget.Toolbar;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;


/**
 * 新增共享 视图
 * Created by lx on 2017/9/4.
 */

public class AddShareDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_add_share_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.text_cancel);

        setRightMenuTexts(R.color.main_green, "保存");
        setTitle("共享");

        setOnClickListener(getActivity(), R.id.ll_permission);
    }


}

