package com.hjhq.teamface.project.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 选择模块
 *
 * @author Administrator
 * @date 2018/5/3
 */

public class SelectModuleDelegate extends AppDelegate {
    public RecyclerView systemRecyclerView;
    public RecyclerView myRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_select_module_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("选择模块");

        systemRecyclerView = get(R.id.system_recycler_view);
        myRecyclerView = get(R.id.my_recycler_view);


        systemRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        myRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    public void setSystemAdapter(BaseQuickAdapter adapter) {
        systemRecyclerView.setAdapter(adapter);
    }

    public void setMyAdapter(BaseQuickAdapter adapter) {
        myRecyclerView.setAdapter(adapter);
    }

}
