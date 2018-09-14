package com.hjhq.teamface.email.view;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.email.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：邮件详情视图
 */

public class ChooseApprovalDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.email_choose_approval_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initWidget() {
        super.initWidget();

    }
}
