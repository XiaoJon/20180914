package com.hjhq.teamface.statistic.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.bean.ReportDetailResultBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 报表详情
 *
 * @author Administrator
 * @date 2018/3/19
 */

public class ReportDetailActivity extends ActivityPresenter<ReportDetailDelegate, StatisticsModel> implements View.OnClickListener {
    private String reportId;
    private Object dataDetail;
    private Object layoutDetail;
    private Map<String, Object> queryWhere;
    private String name;
    private boolean isFinish;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            reportId = getIntent().getStringExtra(Constants.DATA_ID);
            name = getIntent().getStringExtra(Constants.NAME);
        }
    }

    @Override
    public void init() {
        viewDelegate.initFilter(this, reportId);
        viewDelegate.setTitle(name);
        getDetail();
    }

    /**
     * 获取详情数据
     */
    private void getDetail() {
        model.getReportLayoutDetail(this, reportId, new ProgressSubscriber<ReportDetailResultBean>(this) {
            @Override
            public void onNext(ReportDetailResultBean baseBean) {
                super.onNext(baseBean);
                layoutDetail = baseBean.getData();
                handleData();
            }
        });

        Map<String, Object> map = new HashMap<>(2);
        map.put("reportId", reportId);
        map.put("queryWhere", queryWhere);
        model.getReportDataDetail(this, map, new ProgressSubscriber<ReportDetailResultBean>(this) {
            @Override
            public void onNext(ReportDetailResultBean baseBean) {
                super.onNext(baseBean);
                dataDetail = baseBean.getData();
                handleData();
            }
        });
    }

    private synchronized void handleData() {
        if (!isFinish || layoutDetail == null || dataDetail == null) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(layoutDetail + "");
        if (jsonObject == null) {
            ToastUtils.showError(this, "数据异常！");
            return;
        }

        int reportType = jsonObject.getIntValue("reportType");

        JSONObject json = new JSONObject();
        json.put("reportType", reportType);
        json.put("chartList", jsonObject);

        String dataDetailString = JSON.toJSONString(dataDetail);
        LogUtil.d("-----------" + dataDetailString);
        LogUtil.d("-----------" + json.toString());

        viewDelegate.mWebView.evaluateJavascript("javascript:getTablesVal(" + dataDetailString + "," + json.toString() + ")", value -> {
            //此处为 js 返回的结果
            LogUtil.d("此处为 js 返回的结果" + value);
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.tv_filter);
        viewDelegate.setWebClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFinish = true;
                handleData();
            }

        });
    }

    @Override
    public void onClick(View v) {
        viewDelegate.openDrawer();
    }

    @Override
    public void onBackPressed() {
        if (!viewDelegate.closeDrawer()) {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean messageBean) {
        int responseCode = messageBean.getCode();
        //筛选回调
        if (responseCode == StatisticFilterFragment.FILTER_DATA) {
            queryWhere = (Map<String, Object>) messageBean.getObject();
            viewDelegate.closeDrawer();
            getDetail();
        }
    }

    @Override
    protected void onDestroy() {
        viewDelegate.mWebView.destroy();
        viewDelegate.mWebView = null;
        super.onDestroy();
    }
}
