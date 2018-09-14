package com.hjhq.teamface.email.view;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;


/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class EmailListFragmentDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.email_list_fragment;
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
