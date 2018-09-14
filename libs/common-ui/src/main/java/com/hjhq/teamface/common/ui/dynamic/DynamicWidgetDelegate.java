package com.hjhq.teamface.common.ui.dynamic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 动态 视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class DynamicWidgetDelegate extends AppDelegate {
    RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_dynamic_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        mRecyclerView = get(R.id.fragment_move_detail_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }

}

