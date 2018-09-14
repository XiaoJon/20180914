package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.presenter.add.AddProjectActivity;
import com.hjhq.teamface.project.presenter.task.AddTaskActivity;
import com.hjhq.teamface.project.ui.ProjectDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目
 *
 * @author Administrator
 * @date 2018/4/10
 */
@RouteNode(path = "/main", desc = "项目首页")
public class ProjectActivity extends ActivityPresenter<ProjectDelegate, ProjectModel> implements View.OnClickListener {
    List<Fragment> fragments = new ArrayList<>(2);
    private String keyword;

    @Override
    public void init() {
        fragments.add(ListTempFragment.newInstance(0));
        fragments.add(ListTempFragment.newInstance(1));

        viewDelegate.initFilter();
        viewDelegate.setAdapter(fragments);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.iv_left_bar, R.id.tv_project, R.id.tv_task, R.id.iv_add_bar, R.id.iv_search_bar);
        viewDelegate.mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                keyword = "";
            }

            @Override
            public void cancel() {
                viewDelegate.hideSearch();
            }

            @Override
            public void search() {
                ((ListTempFragment) fragments.get(0)).search(keyword);
                SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_search_in_searchbar));
            }

            @Override
            public void getText(String text) {
                keyword = text;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_left_bar) {
            finish();
        } else if (id == R.id.tv_project) {
            viewDelegate.setCurrentItem(0);
        } else if (id == R.id.tv_task) {
            viewDelegate.setCurrentItem(1);
        } else if (id == R.id.iv_add_bar) {
            if (viewDelegate.getCurrentItem() == 0) {
                CommonUtil.startActivtiyForResult(mContext, AddProjectActivity.class, Constants.REQUEST_CODE1, new Bundle());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN);
                CommonUtil.startActivtiyForResult(mContext, AddTaskActivity.class, ProjectConstants.ADD_TASK_TASK_REQUEST_CODE, bundle);
            }
        } else if (id == R.id.iv_search_bar) {
            if (viewDelegate.getCurrentItem() == 0) {
                viewDelegate.showSearch();
            } else {
                viewDelegate.openDrawer();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!viewDelegate.closeDrawer()) {
            super.onBackPressed();
        }
    }

    /**
     * 筛选
     */
    public void filter() {
        viewDelegate.closeDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            //新增项目，刷新列表
            ((ListTempFragment) fragments.get(0)).refreshData();
        } else if (requestCode == ProjectConstants.ADD_TASK_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            //新增个人任务,刷新列表
            ((ListTempFragment) fragments.get(1)).refreshData();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean event) {
        if (ProjectConstants.PERSONAL_TASK_FILTER_CODE == event.getCode()) {
            viewDelegate.closeDrawer();
        }
    }
}
