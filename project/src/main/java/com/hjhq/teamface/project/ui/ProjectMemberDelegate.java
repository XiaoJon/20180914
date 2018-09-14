package com.hjhq.teamface.project.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 项目成员管理
 */

public class ProjectMemberDelegate extends AppDelegate {
    private RecyclerView mRvList;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_member_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_member_manager);
        setRightMenuTexts(mContext.getString(R.string.add));

        mRvList = get(R.id.rv);
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void setItemClick(RecyclerView.OnItemTouchListener listener) {
        mRvList.addOnItemTouchListener(listener);
    }


    public void setAdapter(BaseQuickAdapter mAdapter) {
        mRvList.setAdapter(mAdapter);
    }
}
