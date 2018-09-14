package com.hjhq.teamface.oa.approve.ui;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 筛选
 */

public class ApproveFilterFragment extends FragmentPresenter<FilterDelegate, ApproveModel> {
    public static final int APPROVE_FILTER_DATA = 0x6541;
    private int type;
    private Map<Integer, FilterFieldResultBean> dataMap = new HashMap<>();

    @Override
    protected void init() {
        type = getArguments().getInt(Constants.DATA_TAG1);
        getFilterData();
    }

    /**
     * 得到筛选数据
     */
    public void getFilterData() {
        model.querySearchMenu(((ActivityPresenter) getActivity()), type, new ProgressSubscriber<FilterFieldResultBean>(getActivity(), false) {
            @Override
            public void onNext(FilterFieldResultBean filterDataBean) {
                super.onNext(filterDataBean);
                dataMap.put(type, filterDataBean);
                viewDelegate.initFilerData(getActivity(), filterDataBean);
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
            EventBusUtils.sendEvent(new MessageBean(APPROVE_FILTER_DATA, type + "", map));
        });
    }

    public void setType(int type) {
        this.type = type;
        FilterFieldResultBean filterFieldResultBean = dataMap.get(type);
        if (filterFieldResultBean == null) {
            getFilterData();
        } else {
            viewDelegate.initFilerData(getActivity(), filterFieldResultBean);
        }

    }

}
