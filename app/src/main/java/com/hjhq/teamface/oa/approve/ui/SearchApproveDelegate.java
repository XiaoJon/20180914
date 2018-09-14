package com.hjhq.teamface.oa.approve.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.SearchBar;

/**
 * @author Administrator
 * @date 2018/6/20
 */

public class SearchApproveDelegate extends AppDelegate {
    RecyclerView mRvContacts;
    SearchBar mSearchBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_quote_approve_search;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.approval);
        setRightMenuTexts(mContext.getString(R.string.confirm));


        mRvContacts = get(R.id.search_result_recycler_view);
        mSearchBar = get(R.id.search_bar);

        mSearchBar.setCancelVis(false);
        mRvContacts.setLayoutManager(new LinearLayoutManager(mContext));
    }

    protected void setAdapter(BaseQuickAdapter adapter) {
        mRvContacts.setAdapter(adapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        TextView tvTitle = emptyView.findViewById(R.id.title_tv);
        TextUtil.setText(tvTitle, "输入关键字进行搜索~");
        adapter.setEmptyView(emptyView);
    }
}
