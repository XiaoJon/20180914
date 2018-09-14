package com.hjhq.teamface.common.ui.dynamic;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 动态 视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class DynamicDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshWidget;
    LinearLayout llSort;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_dynamic_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        mRecyclerView = get(R.id.fragment_move_detail_rv);
        swipeRefreshWidget = get(R.id.swipe_refresh_widget);
        llSort = get(R.id.ll_sort);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshWidget.setColorSchemeResources(R.color.main_green);

    }

    /**
     * 设置数量显示
     */
    public void setSortInfo(int count) {
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
    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

}

