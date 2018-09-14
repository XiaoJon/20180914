package com.hjhq.teamface.project.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2018/4/10.
 * 项目文件夹(一级)
 */

public class ProjectFolderListActivityDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRvNavi;


    @Override
    public int getRootLayoutId() {
        return R.layout.project_folder_list_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("文件夹名称");
        mRecyclerView = get(R.id.recycler_view);
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mRvNavi = get(R.id.rv_navi);
        mRvNavi.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public void setOrientation(int orientation) {
                super.setOrientation(HORIZONTAL);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * 导航栏数据
     *
     * @param adapter
     */
    public void setNaviAdapter(BaseQuickAdapter adapter) {
        mRvNavi.setAdapter(adapter);
    }

    /**
     * @param listener
     */
    public void setNaviClick(RecyclerView.OnItemTouchListener listener) {
        mRvNavi.addOnItemTouchListener(listener);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemClickListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);

    }
}
