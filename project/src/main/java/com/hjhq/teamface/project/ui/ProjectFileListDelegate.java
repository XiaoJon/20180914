package com.hjhq.teamface.project.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2018/4/10.
 */

public class ProjectFileListDelegate extends AppDelegate {
    private RecyclerView mRecyclerView;


    @Override
    public int getRootLayoutId() {
        return R.layout.project_file_list_fragment_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    public void setAdapter(BaseQuickAdapter ad) {
        mRecyclerView.setAdapter(ad);
    }

    public void setItemClickListener(RecyclerView.OnItemTouchListener l) {
        mRecyclerView.addOnItemTouchListener(l);
    }
}
