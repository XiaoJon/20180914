package com.hjhq.teamface.project.presenter.navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.SaveSubNodeRequestBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.navigation.EditGroupDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增任务列表
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class AddTaskTempActivity extends ActivityPresenter<EditGroupDelegate, ProjectModel> {
    private long projectId;
    private long nodeId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            projectId = getIntent().getLongExtra(ProjectConstants.PROJECT_ID, 0);
            nodeId = getIntent().getLongExtra(ProjectConstants.NODE_ID, 0);
        }
    }

    @Override
    public void init() {
        viewDelegate.setHint(getString(R.string.project_task_list_name));
        viewDelegate.setTitle(R.string.project_add_task_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addNode();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加节点
     */
    private void addNode() {
        String content = viewDelegate.getContent();
        if (TextUtil.isEmpty(content)) {
            ToastUtils.showError(this, "请输入列表名称");
            return;
        }
        SaveSubNodeRequestBean bean = new SaveSubNodeRequestBean();
        bean.setNodeId(nodeId);
        bean.setProjectId(projectId);
        List<SaveSubNodeRequestBean.SubnodeArrBean> list = new ArrayList<>();
        SaveSubNodeRequestBean.SubnodeArrBean subNode = new SaveSubNodeRequestBean.SubnodeArrBean(content);
        list.add(subNode);
        bean.setSubnodeArr(list);

        //主节点
        model.addSubNode(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showToast(mContext, "新增成功！");
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
