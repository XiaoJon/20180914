package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.adapter.QuoteTaskAdapter;
import com.hjhq.teamface.project.bean.QuoteTaskListResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.QuoteTaskDelegate;

/**
 * 引用任务
 *
 * @author Administrator
 * @date 2018/6/21
 */

public class QuoteTaskActivity extends ActivityPresenter<QuoteTaskDelegate, TaskModel> {
    private QuoteTaskAdapter adapter;

    @Override
    public void init() {
        adapter = new QuoteTaskAdapter(null);
        viewDelegate.setAdapter(adapter);
        queryTaskList();
    }

    /**
     * 查询全部 个人任务和项目任务
     */
    private void queryTaskList() {
        model.queryTaskList(mContext, new ProgressSubscriber<QuoteTaskListResultBean>(mContext) {
            @Override
            public void onNext(QuoteTaskListResultBean bean) {
                super.onNext(bean);
                CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), bean.getData());
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                QuoteTaskListResultBean.DataBean data = (QuoteTaskListResultBean.DataBean) adapte.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(ProjectConstants.PROJECT_ID, data.getProjectId());
                bundle.putString(Constants.DATA_TAG1, data.getTitle());
                CommonUtil.startActivtiyForResult(mContext, SelectTaskActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && Constants.REQUEST_CODE1 == requestCode) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
