package com.hjhq.teamface.customcomponent.select;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.adapter.SelectAdapter;

/**
 *
 * @author lx
 * @date 2017/9/4
 */

public class SelectDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public SelectAdapter selectAdapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_activity_select;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.main_green, getActivity().getString(R.string.commit));
        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("请选择");


        mRecyclerView = get(R.id.recyclerview);

        selectAdapter = new SelectAdapter(null);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(selectAdapter);
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        selectAdapter.setEmptyView(emptyView);
    }

    /**
     * 隐藏menu
     */
    public void hideRightMenu(){
        showMenu();
    }

}