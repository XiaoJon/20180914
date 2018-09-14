package com.hjhq.teamface.project.ui.task;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.MylayoutManager;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.project.R;

/**
 * @author Administrator
 * @date 2018/6/20
 */

public class SearchTaskDelegate extends AppDelegate {
    public RecyclerView mRvContacts;
    public SearchBar mSearchBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_task_quote_search;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.task);
        setRightMenuTexts(mContext.getString(R.string.confirm));


        mRvContacts = get(R.id.search_result_recycler_view);
        mSearchBar = get(R.id.search_bar);
        mSearchBar.setCancelVis(false);

        mRvContacts.setLayoutManager(new MylayoutManager(mContext));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRvContacts.setAdapter(adapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        TextView tvtitle = emptyView.findViewById(R.id.title_tv);
        TextUtil.setText(tvtitle, "输入关键字进行搜索~");
        adapter.setEmptyView(emptyView);
    }
}
