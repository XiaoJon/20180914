package com.hjhq.teamface.project.presenter.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.navigation.EditGroupDelegate;

/**
 * 编辑任务列表名称
 *
 * @author Administrator
 * @date 2018/4/16
 */
public class EditTaskTempActivity extends ActivityPresenter<EditGroupDelegate, ProjectModel> {
    private String navigation;
    private long projectId;
    private long nodeId;
    private long subNodeId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            navigation = intent.getStringExtra(Constants.DATA_TAG1);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            nodeId = intent.getLongExtra(ProjectConstants.NODE_ID, 0);
            subNodeId = intent.getLongExtra(ProjectConstants.SUBNODE_ID, 0);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle(R.string.edit);
        viewDelegate.setNavigation(navigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editNode();
        return super.onOptionsItemSelected(item);
    }


    /**
     * 编辑节点
     */
    private void editNode() {
        String name = viewDelegate.getContent();
        if (TextUtil.isEmpty(name)) {
            ToastUtils.showError(this, "请输入分组名称");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("nodeId", nodeId);
        jsonObject.put("subnodeId", subNodeId);
        jsonObject.put("name", name);

        //主节点
        model.editSubNode(this, jsonObject, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(mContext, "修改成功！");
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1,new NodeBean(projectId, nodeId, subNodeId, name));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
