package com.hjhq.teamface.statistic.ui;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.statistic.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 筛选
 *
 * @author Administrator
 */

public class StatisticFilterFragment extends FragmentPresenter<StatisticFilterDelegate, StatisticsModel> {
    public static final int FILTER_DATA = 0x1654;
    private String reportId;

    @Override
    protected void init() {
        reportId = getArguments().getString(Constants.DATA_TAG1);
        getFilterData();
    }

    public void getFilterData() {
        model.getFilterFields(((ActivityPresenter) getActivity()), reportId, new ProgressSubscriber<FilterFieldResultBean>(getActivity(), false) {
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
            Map<String, Object> map = new HashMap<>(keys.size());
            for (String key : keys) {
                Object o = json.get(key);
                map.put(key, o);
            }
            EventBusUtils.sendEvent(new MessageBean(FILTER_DATA, Constants.DATA_TAG1, map));
        });
    }

}
