package com.hjhq.teamface.attendance.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddWifiDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_wifi_activity;
    }

    RecyclerView mRecyclerView;

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_add_wifi_title);
        // TODO: 2018/6/8 蓝色带返回字样切图 
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        setRightMenuTexts(R.color.app_blue, "刷新", "保存");
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);

    }

    public void setOnItemClick(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

}
