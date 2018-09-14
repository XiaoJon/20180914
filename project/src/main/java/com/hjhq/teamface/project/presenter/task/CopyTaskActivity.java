package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.SelectGroupTempDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 复制任务
 * Created by Administrator on 2018/7/12.
 */

public class CopyTaskActivity extends ActivityPresenter<SelectGroupTempDelegate, TaskModel> implements View.OnClickListener {
    private long taskId;
    private long projectId;
    private long nodeId;
    /**
     * 主节点集合
     */
    private ArrayList<NodeBean> allNodeList;
    /**
     * 子节点集合
     */
    private List<NodeBean> subNodeList;
    /**
     * 子列表
     */
    private List<NodeBean> subTempList;
    private long subNodeId;
    private long subTempId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
        }
    }


    @Override
    public void init() {
        viewDelegate.setTitle("复制任务");
        model.getAllNode(mContext, projectId, new ProgressSubscriber<AllNodeResultBean>(mContext) {
            @Override
            public void onNext(AllNodeResultBean allNodeResultBean) {
                super.onNext(allNodeResultBean);
                allNodeList = allNodeResultBean.getData().getDataList();
            }

            @Override
            protected void showErrorToast(Throwable e) {
                ToastUtils.showError(mContext, "任务分组列表信息获取失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        if (id == R.id.ll_group) {
            bundle.putSerializable(Constants.DATA_TAG1, allNodeList);
            bundle.putInt(Constants.DATA_TAG2, 0);
            CommonUtil.startActivtiyForResult(mContext, SelectNodeActivity.class, Constants.REQUEST_CODE1, bundle);
        } else if (id == R.id.ll_temp) {
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) subNodeList);
            bundle.putInt(Constants.DATA_TAG2, 1);
            CommonUtil.startActivtiyForResult(mContext, SelectNodeActivity.class, Constants.REQUEST_CODE2, bundle);
        } else if (id == R.id.ll_sub_temp) {
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) subTempList);
            bundle.putInt(Constants.DATA_TAG2, 2);
            CommonUtil.startActivtiyForResult(mContext, SelectNodeActivity.class, Constants.REQUEST_CODE3, bundle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        copyTask();
        return super.onOptionsItemSelected(item);
    }

    private void copyTask() {
        if (nodeId == 0) {
            ToastUtils.showError(mContext, "请选择分组");
            return;
        }
        if (subNodeId == 0) {
            ToastUtils.showError(mContext, "请选择列表");
            return;
        }
        if (subTempList!=null && subTempId == 0) {
            ToastUtils.showError(mContext, "请选择子列表");
            return;
        }

        model.copyTask(mContext, taskId, subTempId == 0?nodeId:subNodeId, subTempId == 0?subNodeId:subTempId, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "复制成功");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE1) {
            allNodeList = (ArrayList<NodeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(allNodeList).filter(NodeBean::isCheck).subscribe(node -> {
                clearSubNode();
                clearSubTemp();
                nodeId = node.getId();
                viewDelegate.setNodeName(node.getName());
                subNodeList = node.getSubnodeArr();
                viewDelegate.setTempVisibility(!CollectionUtils.isEmpty(subNodeList));
                viewDelegate.setSubTempVisbility(false);
            });
        } else if (requestCode == Constants.REQUEST_CODE2) {
            subNodeList = (ArrayList<NodeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(subNodeList).filter(NodeBean::isCheck).subscribe(subNode -> {
                clearSubTemp();
                subNodeId = subNode.getId();
                viewDelegate.setSubNodeName(subNode.getName());

                boolean equals = "1".equals(subNode.getChildren_data_type());
                if (equals) {
                    subTempList = subNode.getSubnodeArr();
                } else {
                    subTempList = null;
                }
                viewDelegate.setSubTempVisbility(equals);
            });
        } else if (requestCode == Constants.REQUEST_CODE3) {
            subTempList = (ArrayList<NodeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(subTempList).filter(NodeBean::isCheck).subscribe(subTemp -> {
                subTempId = subTemp.getId();
                viewDelegate.setSubTempName(subTemp.getName());
            });
        }
    }

    private void clearSubTemp() {
        subTempId = 0;
        viewDelegate.clearSubTemp();
        if (subTempList != null) {
            subTempList.clear();
        }
    }

    private void clearSubNode() {
        subNodeId = 0;
        viewDelegate.clearSubNode();
        if (subNodeList != null) {
            subNodeList.clear();
        }
    }

}
