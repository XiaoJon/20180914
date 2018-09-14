package com.hjhq.teamface.statistic.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.bean.ChartDataResultBean;
import com.hjhq.teamface.statistic.bean.ChartListResultBean;
import com.hjhq.teamface.statistic.bean.MenuBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.List;

import rx.Observable;

/**
 * 图标列表
 *
 * @author lx
 * @date 2017/8/31
 */
public class ChartTempFragment extends FragmentPresenter<ChartTempDelegate, StatisticsModel> {
    final static int REQUEST_CHART_SORT_CODE = 0X6578;
    private List<MenuBean> dataList;
    private boolean isFinish;
    private String json;
    private String menuId;

    @Override
    public void init() {
        loadTempData();
    }

    /**
     * 加载菜单
     */
    private void loadTempData() {
        model.findAll((RxAppCompatActivity) getActivity(), new ProgressSubscriber<ChartListResultBean>(getActivity()) {
            @Override
            public void onNext(ChartListResultBean baseBean) {
                super.onNext(baseBean);
                dataList = baseBean.getData();
                if (!CollectionUtils.isEmpty(dataList)) {
                    MenuBean dataBean = dataList.get(0);
                    if ("0".equals(dataBean.getId())) {
                        //项目分析暂时移除
                        dataList.remove(0);
                    }
                    if (!CollectionUtils.isEmpty(dataList)) {
                        dataBean = dataList.get(0);
                        dataBean.setCheck(true);
                        viewDelegate.setSortInfo(dataBean.getName());
                        menuId = dataBean.getId();
                        getDetail();
                    }
                }
            }
        });
    }

    /**
     * 获取详情数据
     */
    private void getDetail() {
        if (TextUtil.isEmpty(menuId) || "0".equals(menuId)) {
            viewDelegate.setEmpty(true);
            return;
        }
        model.getChartDataDetail((RxAppCompatActivity) getActivity(), menuId, new ProgressSubscriber<ChartDataResultBean>(getActivity()) {
            @Override
            public void onNext(ChartDataResultBean baseBean) {
                super.onNext(baseBean);
                json = baseBean.getData();
                LogUtil.d("-----------" + json);
                viewDelegate.setEmpty(false);
                handResult();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                viewDelegate.setEmpty(true);
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> sortClick(), R.id.ll_sort);
        viewDelegate.setWebClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFinish = true;
                handResult();
            }

        });
    }

    /**
     * 调用js
     */
    private void handResult() {
        if (isFinish && json != null) {
            viewDelegate.evaluateJavascript(json.toString());
        }
    }

    /**
     * 分类选择 点击
     */
    protected void sortClick() {
        if (CollectionUtils.isEmpty(dataList)) {
            ToastUtils.showError(getActivity(), "仪表盘数据为空");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_TAG1, (Serializable) dataList);
        bundle.putInt(Constants.DATA_TAG2, 0);
        CommonUtil.startActivtiyForResult(getActivity(), SelectSortActivity.class, REQUEST_CHART_SORT_CODE, bundle);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHART_SORT_CODE && resultCode == Activity.RESULT_OK) {
            dataList = (List<MenuBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(dataList).filter(MenuBean::isCheck).subscribe(menuBean -> {
                menuId = menuBean.getId();
                viewDelegate.setSortInfo(menuBean.getName());
                getDetail();
            });
        }
    }
}
