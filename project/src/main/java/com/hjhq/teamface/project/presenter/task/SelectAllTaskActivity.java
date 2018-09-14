package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.AllTaskGroupAdapter;
import com.hjhq.teamface.project.bean.PersonalTaskResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.SelectTaskDelegate;

/**
 * 选择任务
 *
 * @author Administrator
 * @date 2018/6/21
 */

public class SelectAllTaskActivity extends ActivityPresenter<SelectTaskDelegate, TaskModel> {
    private AllTaskGroupAdapter allTaskAdapter;

    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;

    /**
     * 项目ID
     *
     */
    private long projectId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            projectId = getIntent().getLongExtra(ProjectConstants.PROJECT_ID, 0);
        }
    }

    @Override
    public void init() {
        allTaskAdapter = new AllTaskGroupAdapter(null);
        allTaskAdapter.setSelect(true);
        viewDelegate.setAdapter(allTaskAdapter);
        //getTaskList();
        requestNetData();
    }

    private void requestNetData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queryType", 0);
        requestNetData(jsonObject);
    }

    private void requestNetData(JSONObject jsonObject) {
        new ProjectModel().queryPersonalTask(mContext, jsonObject, new ProgressSubscriber<PersonalTaskResultBean>(mContext) {
            @Override
            public void onNext(PersonalTaskResultBean bean) {
                super.onNext(bean);
                //关闭筛选
                CollectionUtils.notifyDataSetChanged(allTaskAdapter, allTaskAdapter.getData(), bean.getData());
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.DATA_TAG1, projectId);
            CommonUtil.startActivtiyForResult(mContext, SearchTaskActivity.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.search_rl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && Constants.REQUEST_CODE1 == requestCode) {
            setResult(RESULT_OK, data);
            finish();
        }

    }
}
