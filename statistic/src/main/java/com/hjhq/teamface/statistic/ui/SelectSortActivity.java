package com.hjhq.teamface.statistic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.adapter.SortAdapter;
import com.hjhq.teamface.statistic.bean.ChartListResultBean;
import com.hjhq.teamface.statistic.bean.MenuBean;

import java.io.Serializable;
import java.util.List;

import rx.Observable;

/**
 * 选择排序
 *
 * @author Administrator
 * @date 2018/5/8
 */

public class SelectSortActivity extends ActivityPresenter<SelectSortDelegate, StatisticsModel> {
    /**
     * 0 仪表盘，1报表
     */
    private int statisticType;
    private SortAdapter adapter;
    private List<MenuBean> menuList;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            menuList = (List<MenuBean>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
            statisticType = getIntent().getIntExtra(Constants.DATA_TAG2, 0);
        }
    }

    @Override
    public void init() {
        if (statisticType == 0) {
            viewDelegate.setTitle("选择仪表盘");
            adapter = new SortAdapter(R.layout.statistic_item_select_chart_sort, menuList);
        } else {
            viewDelegate.setTitle("选择报表");
            viewDelegate.mSwipeRefreshLayout.setEnabled(false);
            adapter = new SortAdapter(R.layout.statistic_item_select_report_sort, menuList);
        }
        viewDelegate.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (MenuBean menu : menuList) {
            if (menu.isCheck()) {
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, (Serializable) menuList);
                setResult(RESULT_OK, intent);
                finish();
                return super.onOptionsItemSelected(item);
            }
        }
        if (statisticType == 0) {
            ToastUtils.showError(mContext, "请选择仪表盘");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Observable.from(menuList).subscribe(menu -> menu.setCheck(false));
                menuList.get(position).setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
        //仪表盘排序列表刷新
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            model.findAll(this, new ProgressSubscriber<ChartListResultBean>(this) {
                @Override
                public void onNext(ChartListResultBean baseBean) {
                    super.onNext(baseBean);
                    List<MenuBean> dataList = baseBean.getData();
                    Observable.from(menuList)
                            .filter(MenuBean::isCheck)
                            .subscribe(menu -> Observable
                                    .from(dataList)
                                    .filter(bean -> menu.getId().equals(bean.getId()))
                                    .subscribe(bean -> bean.setCheck(true)));
                    menuList.clear();
                    CollectionUtils.addAll(menuList, dataList);
                    adapter.notifyDataSetChanged();
                }
            });
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });
    }
}
