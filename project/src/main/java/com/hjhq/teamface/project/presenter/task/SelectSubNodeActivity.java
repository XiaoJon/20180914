package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.task.SelectNodeDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2018/7/12.
 */

public class SelectSubNodeActivity extends ActivityPresenter<SelectNodeDelegate, ProjectModel> {

    private long nodeId;
    private NodeBean selectSubNode;
    long subNodeId = 0;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            nodeId = getIntent().getLongExtra(ProjectConstants.NODE_ID, 0);
            Serializable serializableExtra = getIntent().getSerializableExtra(Constants.DATA_TAG1);
            if (serializableExtra != null) {
                selectSubNode = (NodeBean) serializableExtra;
                subNodeId = selectSubNode.getId();
            }
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("选择列");
        getSubNode();
    }

    private void getSubNode() {
        model.getSubNode(mContext, nodeId, new ProgressSubscriber<AllNodeResultBean>(mContext) {
            @Override
            public void onNext(AllNodeResultBean allNodeResultBean) {
                super.onNext(allNodeResultBean);
                ArrayList<NodeBean> dataList = allNodeResultBean.getData().getDataList();

                if (!CollectionUtils.isEmpty(dataList)) {
                    Observable.from(dataList).filter(data -> data.getId() == subNodeId).subscribe(data -> data.setCheck(true));
                }
                viewDelegate.setNewData(dataList);
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                selectSubNode = (NodeBean) adapter.getItem(position);
                List<NodeBean> nodeBeanList = adapter.getData();
                Observable.from(nodeBeanList).subscribe(data -> data.setCheck(false));
                selectSubNode.setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectSubNode();
        return super.onOptionsItemSelected(item);
    }

    private void selectSubNode() {
        if (selectSubNode == null) {
            ToastUtils.showError(mContext, "请选择列");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, selectSubNode);
        setResult(RESULT_OK, intent);
        finish();
    }
}
