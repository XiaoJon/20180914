package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;

import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批流程
 *
 * @author Administrator
 * @date 2018/1/26
 */

@RouteNode(path = "/approve/task", desc = "审批流程")
public class ApproveTaskActivity extends ActivityPresenter<ApproveTaskDelegate, ApproveModel> {
    private String moduleBean;
    private String moduleDataId;
    /**
     * 流程实例id
     */
    protected String processInstanceId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(ApproveConstants.MODULE_BEAN);
            moduleDataId = intent.getStringExtra(ApproveConstants.APPROVAL_DATA_ID);
            processInstanceId = intent.getStringExtra(ApproveConstants.PROCESS_INSTANCE_ID);
        }
    }

    @Override
    public void init() {
        getProcessWholeFlow();
    }

    /**
     * 获取审批流
     */
    private void getProcessWholeFlow() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("dataId", moduleDataId);
        map.put("moduleBean", moduleBean);
        map.put("processInstanceId", processInstanceId);
        model.getProcessWholeFlow(this, map, new ProgressSubscriber<ProcessFlowResponseBean>(this) {
            @Override
            public void onNext(ProcessFlowResponseBean baseBean) {
                super.onNext(baseBean);
                List<ProcessFlowResponseBean.DataBean> data = baseBean.getData();
                viewDelegate.setApproveTaskFlow(data);
            }
        });
    }

}
