package com.hjhq.teamface.statistic.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.statistic.R;


/**
 * 报表 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class ReportTempDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.statistic_activity_report_temp;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_green);

    }

    /**
     * 设置排列显示
     */
    public void setSortInfo(String str) {
        TextView sortTitle = get(R.id.tv_sort_title);
        TextUtil.setText(sortTitle, str);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }
}
