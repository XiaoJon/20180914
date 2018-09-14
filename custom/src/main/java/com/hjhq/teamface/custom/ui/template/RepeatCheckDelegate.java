package com.hjhq.teamface.custom.ui.template;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.custom.R;

/**
 * 查重视图代理类
 */

public class RepeatCheckDelegate extends AppDelegate {
    private RecyclerView mRecyclerView;
    private SearchBar mSearchBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_repeat_check;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        mSearchBar = get(R.id.search_bar);
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setHintText(String hint) {
        mSearchBar.setHintText(hint);
    }

    public void setSearchText(String text) {
        if (text == null) {
            return;
        }
        mSearchBar.setText(text);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}
