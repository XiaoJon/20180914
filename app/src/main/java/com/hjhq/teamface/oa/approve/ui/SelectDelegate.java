package com.hjhq.teamface.oa.approve.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by lx on 2017/9/4.
 */

public class SelectDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_custom_select;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    public void setAdapter(BaseQuickAdapter adapter){
        mRecyclerView.setAdapter(adapter);
    }
}