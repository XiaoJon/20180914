package com.hjhq.teamface.project.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2018/4/10.
 * 项目文件夹(一级)/项目分享
 */

public class ProjectFolderListFragmentDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mRefreshLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_folder_list_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recycler_view);
        mRefreshLayout = get(R.id.swipe_refresh_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemClickListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);

    }
}
