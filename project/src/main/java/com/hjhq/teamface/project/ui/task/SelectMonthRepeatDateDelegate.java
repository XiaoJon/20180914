package com.hjhq.teamface.project.ui.task;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 选择每月重复日期
 * Created by Administrator on 2018/7/17.
 */

public class SelectMonthRepeatDateDelegate extends AppDelegate {

    public RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_select_month_repeat_date;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_select_repeat_date);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 7));

    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}
