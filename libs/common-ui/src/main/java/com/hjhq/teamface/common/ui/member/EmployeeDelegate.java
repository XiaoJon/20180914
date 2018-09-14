package com.hjhq.teamface.common.ui.member;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.view.search.SearchEditText;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 人员
 *
 * @author lx
 * @date 2017/9/30
 */

public class EmployeeDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public SearchEditText mSearch;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_employee;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        setRightMenuTexts(R.color.main_green, "确定");
        mRecyclerView = get(R.id.recycler_view);
        mSearch = get(R.id.search_edit_text);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}
