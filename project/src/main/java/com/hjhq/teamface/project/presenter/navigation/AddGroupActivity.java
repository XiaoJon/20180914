package com.hjhq.teamface.project.presenter.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.FlowNodeData;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.navigation.AddGroupDelegate;

import rx.Observable;

/**
 * 新增节点
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class AddGroupActivity extends ActivityPresenter<AddGroupDelegate, ProjectModel> {
    private long projectId;
    private FlowNodeData flowNodeData;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            projectId = getIntent().getLongExtra(ProjectConstants.PROJECT_ID, 0);
        }
    }

    @Override
    public void init() {
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> {
            if (viewDelegate.isAddFinish()) {
                viewDelegate.addTaskList();
            } else {
                ToastUtils.showError(mContext, "请输入列表名称");
            }
        }, R.id.ll_add_task_list);
        viewDelegate.setOnClickListener(v -> CommonUtil.startActivtiyForResult(mContext, SelectWorkFlowActivity.class, Constants.REQUEST_CODE1), R.id.ll_template);

        viewDelegate.setOnClickListener(v -> {
            viewDelegate.clearFlow();
            flowNodeData = null;
        }, R.id.iv_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addGroup();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加节点
     */
    private void addGroup() {
        String content = viewDelegate.getContent();
        if (TextUtil.isEmpty(content)) {
            ToastUtils.showError(this, "请输入分组名称");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", projectId);
        jsonObject.put("name", content);

        if (flowNodeData == null) {
            JSONArray taskListName = viewDelegate.getTaskListName();
            jsonObject.put("subnodeArr", taskListName);
            jsonObject.put("flowStatus", 0);
        } else {
            jsonObject.put("flowStatus", 1);
            jsonObject.put("flowId", flowNodeData.getWorkflow().getId());
            jsonObject.put("subnodeArr", flowNodeData.getNodes());
            JSONArray flowJsonArray = new JSONArray();
            Observable.from(flowNodeData.getNodes()).subscribe(flowNode -> {
                JSONObject flowJsonObject = new JSONObject();
                flowJsonObject.put("name", flowNode.getText());
                flowJsonArray.add(flowJsonObject);
            });
            jsonObject.put("subnodeArr", flowJsonArray);
        }
        //主节点
        model.addMainNode(this, jsonObject, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(mContext, "新增成功！");
                NodeBean bean = new NodeBean();
                bean.setProject_id(projectId);
                EventBusUtils.sendEvent(new MessageBean(EventConstant.PROJECT_MAIN_NODE_ADD_TAG, null, bean));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            flowNodeData = (FlowNodeData) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.showFlow(flowNodeData.getWorkflow().getName());
        }
    }
}
