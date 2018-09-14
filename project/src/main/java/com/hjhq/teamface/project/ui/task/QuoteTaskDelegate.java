package com.hjhq.teamface.project.ui.task;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 引用任务
 *
 * @author Administrator
 * @date 2018/6/21
 */
public class QuoteTaskDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout swipeRefreshWidget;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_quote_task;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("协作");

        swipeRefreshWidget = get(R.id.swipe_refresh_widget);
        swipeRefreshWidget.setColorSchemeResources(R.color.main_green);

        mRecyclerView = get(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
    }
}
