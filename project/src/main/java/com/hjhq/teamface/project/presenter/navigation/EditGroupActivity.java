package com.hjhq.teamface.project.presenter.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.navigation.EditGroupDelegate;

/**
 * 编辑节点
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class EditGroupActivity extends ActivityPresenter<EditGroupDelegate, ProjectModel> {
    private String navigation;
    private long projectId;
    private long nodeId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            navigation = intent.getStringExtra(Constants.DATA_TAG2);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            nodeId = intent.getLongExtra(ProjectConstants.NODE_ID, 0);
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
        String content = viewDelegate.getContent();
        if (TextUtil.isEmpty(content)) {
            ToastUtils.showError(this, "请输入分组名称");
            return;
        }
        //主节点
        model.editMainNode(this, projectId, nodeId, content, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(mContext, "修改成功！");
                NodeBean bean = new NodeBean(projectId, nodeId, 0, content);
                EventBusUtils.sendEvent(new MessageBean(EventConstant.PROJECT_MAIN_NODE_EDIT_TAG, nodeId + "", bean));
                finish();
            }
        });
    }
}
