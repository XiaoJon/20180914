package com.hjhq.teamface.project.presenter.filter;

import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.TaskFilterAdapter;
import com.hjhq.teamface.project.bean.ConditionBean;
import com.hjhq.teamface.project.bean.PersonalTaskConditionResultBean;
import com.hjhq.teamface.project.bean.TaskConditionResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.filter.FilterDelegate;
import com.hjhq.teamface.project.ui.filter.weight.BaseFilterView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 筛选
 */

public class FilterFragment extends FragmentPresenter<FilterDelegate, ProjectModel> {
    /**
     * 0 个人任务筛选  1 项目任务筛选
     */
    private int fromType;
    private long projectId;
    private RxAppCompatActivity mActivity;
    private TaskFilterAdapter mAdapter;

    /**
     * 项目任务筛选
     *
     * @param projectId 项目ID
     * @return
     */
    public static FilterFragment newInstance(int fromType, long projectId) {
        FilterFragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, fromType);
        bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            fromType = arguments.getInt(Constants.DATA_TAG1, 0);
            projectId = arguments.getLong(ProjectConstants.PROJECT_ID);
        }
    }

    @Override
    protected void init() {
        mActivity = (RxAppCompatActivity) getActivity();
        mAdapter = new TaskFilterAdapter(null);
        viewDelegate.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        if (fromType == 0) {
            //个人任务
            viewDelegate.hideMeSort();
            model.queryPersonalTaskConditions(mActivity, new ProgressSubscriber<PersonalTaskConditionResultBean>(mActivity) {
                @Override
                public void onNext(PersonalTaskConditionResultBean personalTaskConditionResultBean) {
                    super.onNext(personalTaskConditionResultBean);
                    List<TaskConditionResultBean.DataBean> list = new ArrayList<>();
                    TaskConditionResultBean.DataBean dataBean = new TaskConditionResultBean.DataBean();
                    dataBean.setBeanName("任务");
                    dataBean.setCondition(personalTaskConditionResultBean.getData().getCondition());
                    setTaskConditionType(dataBean.getCondition());
                    dataBean.setBean(ProjectConstants.PERSONAL_TASK_BEAN);
                    list.add(dataBean);
                    CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), list);
                }
            });
        } else if (fromType == 1) {
            model.queryProjectTaskConditions(mActivity, projectId, new ProgressSubscriber<TaskConditionResultBean>(mActivity) {
                @Override
                public void onNext(TaskConditionResultBean taskConditionResultBean) {
                    super.onNext(taskConditionResultBean);
                    List<TaskConditionResultBean.DataBean> taskConditionResultBeanData = taskConditionResultBean.getData();
                    if (!CollectionUtils.isEmpty(taskConditionResultBeanData)) {
                        for (TaskConditionResultBean.DataBean dataBean : taskConditionResultBeanData) {
                            setTaskConditionType(dataBean.getCondition());
                        }
                    }
                    CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.getData(), taskConditionResultBeanData);
                }
            });
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> viewDelegate.switchCustomSort(), R.id.rl_custom_sort);
        viewDelegate.setOnClickListener(v -> viewDelegate.switchMeSort(), R.id.rl_me_sort);
        viewDelegate.setOnClickListener(v -> viewDelegate.customSortClick(v.getId()), R.id.rl_create_time_asc, R.id.rl_create_time_desc, R.id.rl_modify_time_asc, R.id.rl_modify_time_desc);
        viewDelegate.setOnClickListener(v -> viewDelegate.meSortClick(v.getId()), R.id.rl_responsible, R.id.rl_created, R.id.rl_participant);

        viewDelegate.setOnClickListener(v -> {
            String sortField = viewDelegate.getCustomSort();
            int timeSort = mAdapter.getTimeSort();
            Map<String, Object> map = new HashMap<>();
            if (timeSort >= 0) {
                map.put("dateFormat", timeSort);
            }

            JSONObject json = new JSONObject();
            JSONObject viewList = mAdapter.getViewList();
            Iterator<String> keys1 = viewList.keySet().iterator();
            try {
                while (keys1.hasNext()) {
                    String next = keys1.next();
                    JSONArray jsonArray = viewList.getJSONArray(next);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        BaseFilterView view = (BaseFilterView) jsonArray.get(i);
                        view.put(json);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.e(json.toString());

            Set<String> keys = json.keySet();
            for (String key : keys) {
                Object o = json.get(key);
                map.put(key, o);
            }

            if (fromType == 0) {
                EventBusUtils.sendEvent(new MessageBean(ProjectConstants.PERSONAL_TASK_FILTER_CODE, sortField, map));
            } else {
                EventBusUtils.sendEvent(new MessageBean(ProjectConstants.PROJECT_TASK_FILTER_CODE, sortField, map));
            }
        }, R.id.tv_confirm);

        viewDelegate.setOnClickListener(v -> {
            mAdapter.reset();
            viewDelegate.reset();
        }, R.id.tv_reset);
    }

    /**
     * 设置任务的条件类型
     *
     * @param list
     */
    private void setTaskConditionType(List<ConditionBean> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (ConditionBean conditionBean : list) {
            if (!TextUtil.isEmpty(conditionBean.getType())) {
                continue;
            }
            switch (conditionBean.getField()) {
                case ProjectConstants.PROJECT_TASK_DEADLINE:
                case ProjectConstants.PROJECT_TASK_STARTTIME:
                case "datetime_create_time":
                case "datetime_modify_time":
                    conditionBean.setType("datetime");
                    break;
                case ProjectConstants.PROJECT_TASK_EXECUTOR:
                case "personnel_create_by":
                case "personnel_modify_by":
                    conditionBean.setType("personnel");
                    break;
                case ProjectConstants.PROJECT_TASK_LABEL:
                case "picklist_priority":
                case "picklist_difficulty":
                    conditionBean.setType("picklist");
                    break;
                default:
                    conditionBean.setType("text");
                    break;
            }
        }

    }
}
