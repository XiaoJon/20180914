package com.hjhq.teamface.project.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.project.adapter.AllTaskGroupAdapter;
import com.hjhq.teamface.project.bean.PersonalTaskResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ListDelegate;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;


/**
 * 所有任务列表
 *
 * @author Administrator
 * @date 2018/4/10
 */
public class AllTaskListFragment extends FragmentPresenter<ListDelegate, ProjectModel> {
    AllTaskGroupAdapter allTaskAdapter;
    private RxAppCompatActivity mActivity;
    private int type;


    public static AllTaskListFragment newInstance(int index) {
        AllTaskListFragment myFragment = new AllTaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {
        mActivity = (RxAppCompatActivity) getActivity();
        allTaskAdapter = new AllTaskGroupAdapter(null);
        allTaskAdapter.setQueryType(type);
        viewDelegate.setAdapter(allTaskAdapter);
        requestNetData();
    }

    /**
     * 请求网络数据
     */
    private void requestNetData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queryType", type);
        requestNetData(jsonObject);
    }

    private void requestNetData(JSONObject jsonObject) {
        model.queryPersonalTask(mActivity, jsonObject, new ProgressSubscriber<PersonalTaskResultBean>(mActivity) {
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
        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        requestNetData();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean event) {
        if (ProjectConstants.PERSONAL_TASK_FILTER_CODE == event.getCode()) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment == null || !(parentFragment instanceof ListTempFragment)) {
                return;
            }
            if (((ListTempFragment) getParentFragment()).getCurrentItem() != type) {
                return;
            }
            String sortField = event.getTag();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("queryType", type);
            jsonObject.put("sortField", sortField);
            jsonObject.put("queryWhere", event.getObject());
            requestNetData(jsonObject);
        } else if (event.getCode() == ProjectConstants.PROJECT_TASK_REFRESH_CODE) {
            List<PersonalTaskResultBean.DataBean> data = allTaskAdapter.getData();
            if (!CollectionUtils.isEmpty(data)) {
                PersonalTaskResultBean.DataBean dataBean = data.get(0);

                Observable.from(dataBean.getTasks()).filter(item -> event.getTag().equals(item.getId() + "")).subscribe(item -> {
                    requestNetData();
                });
            }
        }
    }
}
