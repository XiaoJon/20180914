package com.hjhq.teamface.project.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class ProjectSearchDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.project_search_activity;
    }

    private SearchBar mSearchBar;
    private RecyclerView mRecyclerView;

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mSearchBar = get(R.id.search_bar);
        mRecyclerView = get(R.id.search_result_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setSearchListener(SearchBar.SearchListener listener) {
        mSearchBar.setSearchListener(listener);
    }
}
