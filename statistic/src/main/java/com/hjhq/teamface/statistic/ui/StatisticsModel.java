package com.hjhq.teamface.statistic.ui;

import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.MainRetrofit;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.factory.FastJsonConverterFactory;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.statistic.StatisticsApiService;
import com.hjhq.teamface.statistic.bean.ChartDataResultBean;
import com.hjhq.teamface.statistic.bean.ChartListResultBean;
import com.hjhq.teamface.statistic.bean.ReportDetailResultBean;
import com.hjhq.teamface.statistic.bean.ReportListResultBean;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.Map;

import rx.Subscriber;

/**
 * 统计Model
 *
 * @author lx
 * @date 2017/9/5
 */

public class StatisticsModel implements IModel<StatisticsApiService> {
    @Override
    public StatisticsApiService getApi() {
        return new ApiManager<StatisticsApiService>().getAPI(StatisticsApiService.class);
    }

    /**
     * 获取报表列表
     *
     * @param mActivity
     * @param map
     * @param s
     */
    void getReportList(RxAppCompatActivity mActivity, Map<String, String> map, Subscriber<ReportListResultBean> s) {
        getApi().getReportList(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取仪表盘列表
     *
     * @param mActivity
     * @param s
     */
    void findAll(RxAppCompatActivity mActivity, Subscriber<ChartListResultBean> s) {
        getApi().findAll().map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取筛选字段
     *
     * @param mActivity
     * @param reportId
     * @param s
     */
    public void getFilterFields(RxAppCompatActivity mActivity, String reportId, Subscriber<FilterFieldResultBean> s) {
        getApi().getFilterFields(reportId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取报表定义详情
     *
     * @param mActivity
     * @param reportId
     * @param s
     */
    void getReportLayoutDetail(RxAppCompatActivity mActivity, String reportId, Subscriber<ReportDetailResultBean> s) {
        StatisticsApiService build = new MainRetrofit.Builder<StatisticsApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(StatisticsApiService.class);
        build.getReportLayoutDetail(reportId).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取报表数据详情
     *
     * @param mActivity
     * @param s
     */
    void getReportDataDetail(RxAppCompatActivity mActivity, Map<String, Object> map, Subscriber<ReportDetailResultBean> s) {
        getApi().getReportDataDetail(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取仪表盘数据详情
     *
     * @param mActivity
     * @param s
     */
    void getChartDataDetail(RxAppCompatActivity mActivity, String id, Subscriber<ChartDataResultBean> s) {
        StatisticsApiService build = new MainRetrofit.Builder<StatisticsApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(StatisticsApiService.class);
        build.getChartDataDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
