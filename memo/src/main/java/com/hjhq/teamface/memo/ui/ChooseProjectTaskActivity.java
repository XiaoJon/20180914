package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.adapter.ProjectTaskAdapter;
import com.hjhq.teamface.common.bean.TaskInfoBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.view.ChooseRelevanceDelegate;

import java.util.ArrayList;
import java.util.List;

public class ChooseProjectTaskActivity extends ActivityPresenter<ChooseRelevanceDelegate, MemoModel> {
    private String keyword = "";
    private ProjectTaskAdapter mTaskAdapter;
    private long projectId;

    @Override
    public void init() {
        viewDelegate.showMenu();
        initIntent();
        initAdapter();
        initData();
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            projectId = bundle.getLong(Constants.DATA_TAG1);
        }
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final TaskInfoBean infoBean = mTaskAdapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, projectId);
                intent.putExtra(Constants.DATA_TAG2, infoBean.getId());
                /*intent.putExtra(Constants.DATA_TAG3,infoBean.getNa );
                intent.putExtra(Constants.DATA_TAG4, );
                intent.putExtra(Constants.DATA_TAG5, );
                intent.putExtra(Constants.DATA_TAG6, );
                intent.putExtra(Constants.DATA_TAG7, );*/
                setResult(RESULT_OK, intent);
                finish();
                super.onItemClick(adapter, view, position);
            }
        });
        viewDelegate.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
            }
        });
        viewDelegate.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(keyword)) {
                        return true;
                    } else {
                        initData();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected void initData() {
        model.queryAllTaskList(mContext, projectId,
                new ProgressSubscriber<TaskListBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(TaskListBean projectListBean) {
                        super.onNext(projectListBean);
                        showDataResult(projectListBean);
                    }
                });
    }

    /**
     * 显示数据
     */
    private void showDataResult(TaskListBean projectListBean) {
        TaskListBean.DataBean data = projectListBean.getData();
        List<TaskInfoBean> dataList = data.getDataList();
        mTaskAdapter.getData().clear();
        mTaskAdapter.getData().addAll(dataList);
        mTaskAdapter.notifyDataSetChanged();
    }

    protected void initAdapter() {
        viewDelegate.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTaskAdapter = new ProjectTaskAdapter(new ArrayList<>());
        viewDelegate.setAdapter(mTaskAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
