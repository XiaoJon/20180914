package com.hjhq.teamface.custom.ui.template;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;



public class AutoModuleDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_auto_module;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuIcons(R.drawable.search_icon);

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_green);

    }


    public void setSortInfo(String str) {
        setSortInfo(str, 0);
    }

    public void setSortInfo(int count) {
        setSortInfo(null, count);
    }

    /**
     * 设置排列显示
     */
    public void setSortInfo(String str, int count) {
        TextView sortTitle = get(R.id.tv_sort_title);
        TextUtil.setText(sortTitle, str);
        TextView sortCount = get(R.id.tv_sort_count);
        if (count == 0) {
            sortCount.setVisibility(View.GONE);
        } else {
            sortCount.setVisibility(View.VISIBLE);
            TextUtil.setText(sortCount, count + "条");
        }
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }
}
