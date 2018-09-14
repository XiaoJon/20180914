package com.hjhq.teamface.custom.ui.filter;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.ui.template.DataTempModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 筛选
 */

public class FilterFragment extends FragmentPresenter<FilterDelegate, DataTempModel> {
    public static final int FILTER_DATA = 0x654;
    private String moduleId;

    public static FilterFragment newInstance(String moduleId) {

        FilterFragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, moduleId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init() {
        moduleId = getArguments().getString(Constants.DATA_TAG1);
        getFilterData();
    }

    public void getFilterData() {
        model.getFilterFields(((ActivityPresenter) getActivity()), moduleId, new ProgressSubscriber<FilterFieldResultBean>(getActivity(), false) {
            @Override
            public void onNext(FilterFieldResultBean filterDataBean) {
                super.onNext(filterDataBean);
                viewDelegate.initFilerData(getActivity(), filterDataBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //错误处理
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.tv_ok).setOnClickListener(v -> {
            JSONObject json = new JSONObject();
            viewDelegate.getData(json);
            LogUtil.e(json.toString());

            Set<String> keys = json.keySet();
            Map<String, Object> map = new HashMap<>();
            for (String key : keys) {
                Object o = json.get(key);
                map.put(key, o);
            }
            EventBusUtils.sendEvent(new MessageBean(FILTER_DATA, Constants.DATA_TAG1, map));
        });
    }

}
