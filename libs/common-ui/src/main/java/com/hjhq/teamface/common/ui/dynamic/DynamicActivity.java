package com.hjhq.teamface.common.ui.dynamic;

import android.content.Intent;
import android.os.Bundle;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
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

public class DynamicActivity extends ActivityPresenter<DynamicDelegate, CommonModel> {
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private List<DynamicListResultBean.DataBean.TimeListBean> dataList = new ArrayList<>();//评论数据
    private int state = Constants.NORMAL_STATE;
    private DynamicAdapter adapter;
    private String moduleBean;
    private String objectId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            objectId = intent.getStringExtra(Constants.DATA_ID);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("动态");

        adapter = new DynamicAdapter(dataList);
        viewDelegate.setAdapter(adapter);
        loadData();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //加载更更多
        adapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                adapter.loadMoreComplete();
                adapter.setEnableLoadMore(false);
                return;
            }
            state = Constants.LOAD_STATE;
            loadData();
        }, viewDelegate.mRecyclerView);
        //刷新
        viewDelegate.swipeRefreshWidget.setOnRefreshListener(() -> {
            state = Constants.REFRESH_STATE;
            loadData();
            viewDelegate.swipeRefreshWidget.setRefreshing(false);
        });
    }


    /**
     * 加载数据
     */
    private void loadData() {
        int pageNum = state == Constants.LOAD_STATE ? currentPageNo : 1;

        model.getDynamicList(this, objectId, moduleBean, new ProgressSubscriber<DynamicListResultBean>(this) {
            @Override
            public void onNext(DynamicListResultBean dynamicListResultBean) {
                super.onNext(dynamicListResultBean);
                List<DynamicListResultBean.DataBean> data = dynamicListResultBean.getData();
                List<DynamicListResultBean.DataBean.TimeListBean> list = new ArrayList<>();
                Observable.from(data)
                        .filter(bean -> bean.getTimeList() != null && bean.getTimeList().size() > 0)
                        .subscribe(bean -> {
                            List<DynamicListResultBean.DataBean.TimeListBean> timeList = bean.getTimeList();
                            timeList.get(0).setTimeDate(bean.getTimeDate());
                            list.addAll(bean.getTimeList());
                        });

                switch (state) {
                    case Constants.NORMAL_STATE:
                    case Constants.REFRESH_STATE:
                        dataList.clear();
                        adapter.setEnableLoadMore(true);
                        break;
                    case Constants.LOAD_STATE:
                        adapter.loadMoreEnd();
                        break;
                    default:
                        break;
                }
                dataList.addAll(list);
                adapter.notifyDataSetChanged();


                viewDelegate.setSortInfo(dataList.size());
//                totalPages = bean.getData().getTotalPages();
//                pageNo = bean.getData().getPageNum();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (state == Constants.LOAD_STATE) {
                    adapter.loadMoreFail();
                }
            }
        });
    }
}
