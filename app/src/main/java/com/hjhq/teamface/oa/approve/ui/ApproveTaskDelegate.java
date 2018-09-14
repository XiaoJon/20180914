package com.hjhq.teamface.oa.approve.ui;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.oa.approve.widget.ApproveTaskView;

import java.util.List;


/**
 * 审批流程视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class ApproveTaskDelegate extends AppDelegate {
    private ApproveTaskView approveTaskView;

    @Override
    public int getRootLayoutId() {
        return R.layout.approve_task_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        getToolbar().setNavigationIcon(R.drawable.icon_back_with_text);
        approveTaskView = get(R.id.approve_task_view);
    }

    /**
     * 设置审批流程
     *
     * @param taskFlow
     */
    public void setApproveTaskFlow(List<ProcessFlowResponseBean.DataBean> taskFlow) {
        approveTaskView.setApproveTaskFlow(taskFlow);
    }
}