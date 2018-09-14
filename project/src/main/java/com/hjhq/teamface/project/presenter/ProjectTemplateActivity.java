package com.hjhq.teamface.project.presenter;

import android.support.v4.app.Fragment;

import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ProjectTemplateDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/template", desc = "项目模板")
public class ProjectTemplateActivity extends ActivityPresenter<ProjectTemplateDelegate, ProjectModel> {
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public void init() {
        String[] title = new String[]{"项目模板", "我的模板"};

        fragmentList.add(ProjectTemplateListFragment.newInstance(0));
        fragmentList.add(ProjectTemplateListFragment.newInstance(1));

        viewDelegate.initNavigator(getSupportFragmentManager(), title, fragmentList);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.iv_back).setOnClickListener(v -> {
            finish();
        });
    }
}

