package com.hjhq.teamface.oa.approve.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * 发起审批
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class ApproveTypeDelegate extends AppDelegate {
    RecyclerView appRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_all_function;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        setTitle("发送审批");
        appRecyclerView = get(R.id.app_recycler);
        appRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(getActivity(), R.color.gray_f2, (int) DeviceUtils.dpToPixel(getActivity(), 8)));
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        appRecyclerView.setAdapter(adapter);
    }
}
