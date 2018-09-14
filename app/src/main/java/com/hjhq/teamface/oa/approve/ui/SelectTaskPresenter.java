package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.oa.approve.adapter.SelectTaskAdapter;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * 驳回、通过方式选择
 *
 * @author lx
 * @date 2017/9/8
 */

public class SelectTaskPresenter extends ActivityPresenter<SelectDelegate, ApproveModel> {
    private List<RejectTypeResponseBean.DataBean.HistoricTaskListBean> taskList;
    public SelectTaskAdapter selectAdapter;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            taskList = (List<RejectTypeResponseBean.DataBean.HistoricTaskListBean>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("驳回节点");
        selectAdapter = new SelectTaskAdapter(taskList);
        viewDelegate.setAdapter(selectAdapter);
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                for (int i = 0; i < taskList.size(); i++) {
                    if (i == position) {
                        taskList.get(i).setCheck(true);
                    } else {
                        taskList.get(i).setCheck(false);
                    }
                }
                setResult();
            }
        });
    }

    /**
     * 返回结果
     */
    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, (Serializable) taskList);
        setResult(RESULT_OK, intent);
        finish();
    }

}
