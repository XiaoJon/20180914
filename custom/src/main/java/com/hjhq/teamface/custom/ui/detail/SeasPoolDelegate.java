package com.hjhq.teamface.custom.ui.detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;

/**
 * 动态 视图
 * Created by lx on 2017/9/4.
 */

public class SeasPoolDelegate extends AppDelegate {
    RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_seas_pool_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.recycler_view);

        setRightMenuTexts(R.color.main_green, "确定");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext, R.color.gray_f2, (int) DeviceUtils.dpToPixel(mContext, 1)));
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

}

