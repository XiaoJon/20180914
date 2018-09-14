package com.hjhq.teamface.project.ui;

import android.view.View;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 选择项目角色
 * Created by Administrator on 2018/4/16.
 */

public class ProjectRoleDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_role;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_role);
        setRightMenuTexts(R.color.app_blue,mContext.getString(R.string.confirm));
    }

    public void setRole(int selectRole) {
        if (selectRole == 0) {
            setVisibility(R.id.iv_member, View.VISIBLE);
            setVisibility(R.id.iv_visitor, View.GONE);
        } else {
            setVisibility(R.id.iv_member, View.GONE);
            setVisibility(R.id.iv_visitor, View.VISIBLE);
        }
    }

}
