package com.hjhq.teamface.project.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.RelationListResultBean;

/**
 * 关联任务、被关联任务
 * Created by Administrator on 2018/7/16.
 */

public class RelationTaskAdapter extends BaseQuickAdapter<RelationListResultBean.DataBean.ModuleDataBean, BaseViewHolder> {
    private TaskItemAdapter.OnItemClickListener onItemClickListener;

    public RelationTaskAdapter() {
        super(R.layout.project_task_relevance_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, RelationListResultBean.DataBean.ModuleDataBean item) {
        helper.setText(R.id.tv_task_temp_name, item.getModule_name());
        helper.addOnClickListener(R.id.iv_task_menu);

        RecyclerView relevanceRecyclerView = helper.getView(R.id.recycler_view);
        relevanceRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(item.getModuleDataList(), false);
        relevanceRecyclerView.setAdapter(taskItemAdapter);

        helper.getView(R.id.iv_task_menu).setOnClickListener(v -> helper.setVisible(R.id.recycler_view, relevanceRecyclerView.getVisibility() != View.VISIBLE));

        if (onItemClickListener != null) {
            taskItemAdapter.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemClickListener(TaskItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
