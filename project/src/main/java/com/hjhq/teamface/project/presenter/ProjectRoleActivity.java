package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ProjectRoleDelegate;

/**
 * 选择项目角色
 * Created by Administrator on 2018/4/16.
 */

public class ProjectRoleActivity extends ActivityPresenter<ProjectRoleDelegate, ProjectModel> {
    /**
     * 0 成员 1 访客
     */
    private int selectRole;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            selectRole = getIntent().getIntExtra(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    public void init() {
        viewDelegate.setRole(selectRole);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> {
            selectRole = selectRole == 0 ? 1 : 0;
            viewDelegate.setRole(selectRole);
        }, R.id.rl_visitor, R.id.rl_member);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, selectRole);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
