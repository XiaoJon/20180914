package com.hjhq.teamface.attendance.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.AppDelegate;


/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class AttendanceDataFragmentDelegate extends AppDelegate {
    RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_data_fragment_mine;
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

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemListener(RecyclerView.OnItemTouchListener l) {
        mRecyclerView.addOnItemTouchListener(l);

    }


    public void setItemClickListener(SimpleItemClickListener simpleItemClickListener) {
        mRecyclerView.addOnItemTouchListener(simpleItemClickListener);
    }
}
