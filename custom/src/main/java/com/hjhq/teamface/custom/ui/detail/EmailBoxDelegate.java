package com.hjhq.teamface.custom.ui.detail;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;

/**
 * 自定义跳转邮箱
 *
 * @author Administrator
 * @date 2018/3/20
 */

public class EmailBoxDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public int getRootLayoutId() {
        return R.layout.custom_email_fragment_box;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_green);


    }


    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

}
