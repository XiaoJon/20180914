package com.hjhq.teamface.oa.approve.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;


/**
 * 审批 视图代理类
 * Created by lx on 2017/8/31.
 */

public class ApproveFragmentDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshWidget;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_approve;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        swipeRefreshWidget = get(R.id.swipe_refresh_widget);
        swipeRefreshWidget.setColorSchemeResources(R.color.main_green);

        mRecyclerView = get(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

}
