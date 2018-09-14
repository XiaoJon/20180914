package com.hjhq.teamface.project.ui.navigation;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.NavigationAdapter;

/**
 * 管理分组
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class TaskGroupDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_navigation;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_manage_group);

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext));
    }

    public void setAdapter(NavigationAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }
}
