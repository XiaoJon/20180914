package com.hjhq.teamface.common.ui.dynamic;

import android.os.Bundle;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.adapter.DynamicAdapter;
import com.hjhq.teamface.common.bean.DynamicListResultBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 动态
 * Created by mou on 2017/4/1.
 */

public class DynamicFragment extends FragmentPresenter<DynamicWidgetDelegate, CommonModel> {
    private DynamicAdapter adapter;
    private String moduleBean;
    private String objectId;

    public static DynamicFragment newInstance(String moduleBean, String objectId) {
        DynamicFragment dynamicFragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.DATA_ID, objectId);
        dynamicFragment.setArguments(bundle);
        return dynamicFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            moduleBean = arguments.getString(Constants.MODULE_BEAN);
            objectId = arguments.getString(Constants.DATA_ID);
        }
    }

    @Override
    public void init() {
        adapter = new DynamicAdapter(null);
        viewDelegate.setAdapter(adapter);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        model.getDynamicList((ActivityPresenter) getActivity(), objectId, moduleBean, new ProgressSubscriber<DynamicListResultBean>(getActivity()) {
            @Override
            public void onNext(DynamicListResultBean dynamicListResultBean) {
                super.onNext(dynamicListResultBean);
                List<DynamicListResultBean.DataBean> data = dynamicListResultBean.getData();
                List<DynamicListResultBean.DataBean.TimeListBean> list = new ArrayList<>();
                Observable.from(data)
                        .filter(bean -> !CollectionUtils.isEmpty(bean.getTimeList()))
                        .subscribe(bean -> {
                            List<DynamicListResultBean.DataBean.TimeListBean> timeList = bean.getTimeList();
                            timeList.get(0).setTimeDate(bean.getTimeDate());
                            list.addAll(bean.getTimeList());
                        });
                CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), list);
            }
        });
    }
}
