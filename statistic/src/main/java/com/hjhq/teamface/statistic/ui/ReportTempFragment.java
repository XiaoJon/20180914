package com.hjhq.teamface.statistic.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.adapter.ReportAdapter;
import com.hjhq.teamface.statistic.bean.MenuBean;
import com.hjhq.teamface.statistic.bean.ReportListResultBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 报表列表
 *
 * @author lx
 * @date 2017/8/31
 */
public class ReportTempFragment extends FragmentPresenter<ReportTempDelegate, StatisticsModel> {
    final static int REQUEST_REPORT_SORT_CODE = 0X6518;
    private ReportAdapter reportAdapter;
    /**
     * menu数据集合
     */
    private List<MenuBean> menuList;
    /**
     * 选中的menu 值
     */
    protected String menuId = "0";

    /**
     * 当前页数
     */
    private int currentPageNo = 1;
    /**
     * 总页数
     */
    private int totalPages = 1;
    private int state = Constants.NORMAL_STATE;


    @Override
    public void init() {
        if (reportAdapter == null) {
            reportAdapter = new ReportAdapter(null);
            View emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
            reportAdapter.setEmptyView(emptyView);
            viewDelegate.setAdapter(reportAdapter);
        }

        menuList = new ArrayList<>();
        menuList.add(new MenuBean("0", "最近报表", true));
        menuList.add(new MenuBean("1", "全部报表"));
        menuList.add(new MenuBean("2", "共享给我的报表"));
        menuList.add(new MenuBean("3", "我创建的报表"));
        viewDelegate.setSortInfo(menuList.get(0).getName());
        loadTempData();
    }


    /**
     * 加载列表数据
     */
    private void loadTempData() {
        Map<String, String> map = new HashMap<>(4);
        map.put("menuId", menuId);
        map.put("styleType", "report");

        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        map.put("pageSize", Constants.PAGESIZE + "");
        map.put("pageNum", pageNo + "");
        model.getReportList((RxAppCompatActivity) getActivity(), map, new ProgressSubscriber<ReportListResultBean>(getActivity()) {
            @Override
            public void onNext(ReportListResultBean baseBean) {
                super.onNext(baseBean);
                showDataResult(baseBean);
            }
        });
    }


    /**
     * 显示数据结果
     *
     * @param baseBean
     */
    protected void showDataResult(ReportListResultBean baseBean) {
        ReportListResultBean.DataBean dataBean = baseBean.getData();
        List<ReportListResultBean.DataBean.ListBean> listBean = dataBean.getList();
        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                CollectionUtils.notifyDataSetChanged(reportAdapter, reportAdapter.getData(), listBean);
                break;
            case Constants.LOAD_STATE:
                reportAdapter.addData(listBean);
                reportAdapter.loadMoreComplete();
                break;
            default:
                break;
        }

        PageInfo pageInfo = dataBean.getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> sortClick(), R.id.ll_sort);
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tempItemClick(adapter, view, position);
            }
        });
        //刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });

        //加载更更多
        reportAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                reportAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            loadTempData();
        }, viewDelegate.mRecyclerView);
    }

    /**
     * 数据点击事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    protected void tempItemClick(BaseQuickAdapter adapter, View view, int position) {
        ReportListResultBean.DataBean.ListBean item = reportAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_ID, item.getId());
        bundle.putString(Constants.NAME, item.getReport_label());
        CommonUtil.startActivtiy(getActivity(), ReportDetailActivity.class, bundle);
    }


    /**
     * 分类选择 点击
     */
    protected void sortClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_TAG1, (Serializable) menuList);
        bundle.putInt(Constants.DATA_TAG2, 1);
        CommonUtil.startActivtiyForResult(getActivity(), SelectSortActivity.class, REQUEST_REPORT_SORT_CODE, bundle);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REPORT_SORT_CODE && resultCode == Activity.RESULT_OK) {
            menuList = (List<MenuBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(menuList).filter(MenuBean::isCheck).subscribe(menuBean -> {
                menuId = menuBean.getId();
                viewDelegate.setSortInfo(menuBean.getName());
                refreshData();
            });
        }
    }

    private void refreshData() {
        state = Constants.REFRESH_STATE;
        loadTempData();
    }
}
