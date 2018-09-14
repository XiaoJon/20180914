package com.hjhq.teamface.project.ui.task;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.SelectNodeAdapter;
import com.hjhq.teamface.project.bean.NodeBean;

import java.util.List;

/**
 * 选择任务节点
 * Created by Administrator on 2018/7/12.
 */

public class SelectNodeDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    SelectNodeAdapter adapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_select_node;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(mContext.getString(R.string.confirm));
        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyLinearDeviderDecoration myLinearDeviderDecoration = new MyLinearDeviderDecoration(mContext);
        myLinearDeviderDecoration.setMarginStart((int) DeviceUtils.dpToPixel(mContext, 15));
        mRecyclerView.addItemDecoration(myLinearDeviderDecoration);

        adapter = new SelectNodeAdapter(null);
        setAdapter(adapter);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View inflate = View.inflate(mContext, R.layout.view_workbench_empty, null);
        adapter.setEmptyView(inflate);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置新数据
     *
     * @param list
     */
    public void setNewData(List<NodeBean> list) {
        adapter.setNewData(list);
    }
}
