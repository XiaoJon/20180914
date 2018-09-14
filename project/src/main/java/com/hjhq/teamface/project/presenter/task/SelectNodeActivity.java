package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.task.SelectNodeDelegate;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 选择分组
 * Created by Administrator on 2018/7/12.
 */

public class SelectNodeActivity extends ActivityPresenter<SelectNodeDelegate, ProjectModel> {

    private ArrayList<NodeBean> allNodeList;
    /**
     * 0：分组 1：列表 2: 子列
     */
    private int formType;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            allNodeList = (ArrayList<NodeBean>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
            formType = getIntent().getIntExtra(Constants.DATA_TAG2, 0);
        }
    }

    @Override
    public void init() {
        if (formType == 0) {
            viewDelegate.setTitle("选择分组");
        } else if (formType == 1) {
            viewDelegate.setTitle("选择列");
        } else if (formType == 2) {
            viewDelegate.setTitle("选择子列");
        }
        viewDelegate.setNewData(allNodeList);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                List<NodeBean> nodeBeanList = adapter.getData();
                Observable.from(nodeBeanList).subscribe(data -> data.setCheck(false));

                NodeBean item = (NodeBean) adapter.getItem(position);
                item.setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectNode();
        return super.onOptionsItemSelected(item);
    }

    private void selectNode() {
        final boolean[] isCheck = {false};
        Observable.from(allNodeList).filter(NodeBean::isCheck).subscribe(node -> isCheck[0] = true);
        if (!isCheck[0]) {
            switch (formType) {
                case 0:
                    ToastUtils.showError(mContext, "请选择分组");
                    break;
                case 1:
                    ToastUtils.showError(mContext, "请选择列");
                    break;
                case 2:
                    ToastUtils.showError(mContext, "请选择子列");
                    break;
            }
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, allNodeList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
