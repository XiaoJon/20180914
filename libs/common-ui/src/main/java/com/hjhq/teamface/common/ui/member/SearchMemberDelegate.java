package com.hjhq.teamface.common.ui.member;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;


/**
 * 搜索 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

class SearchMemberDelegate extends AppDelegate {
    RecyclerView resultRv;
    SearchBar mSearchBar;
    EmptyView emptyView;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_search_member;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();

        mSearchBar = get(R.id.search_bar);
        resultRv = get(R.id.rv_search_result);
        resultRv.setLayoutManager(new LinearLayoutManager(mContext));

        emptyView = new EmptyView(mContext);
        emptyView.setEmptyTitle(mContext.getString(R.string.no_search_result));
        emptyView.setEmptyImage(R.drawable.icon_no_search_result);

        mSearchBar.setHintText(mContext.getString(R.string.file_search_hint));
        mSearchBar.setCancelVis(false);
        mSearchBar.requestTextFocus();
    }

    /**
     * 设置结果适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        resultRv.setVisibility(View.VISIBLE);

        adapter.setEmptyView(emptyView);
        resultRv.setAdapter(adapter);
    }

}
