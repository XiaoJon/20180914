package com.hjhq.teamface.statistic;


import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.statistic.bean.ChartDataResultBean;
import com.hjhq.teamface.statistic.bean.ChartListResultBean;
import com.hjhq.teamface.statistic.bean.ReportDetailResultBean;
import com.hjhq.teamface.statistic.bean.ReportListResultBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 统计报表
 *
 * @author xj
 */
public interface StatisticsApiService {

    /**
     * 获取报表列表
     */
    @GET("report/getReportList")
    Observable<ReportListResultBean> getReportList(@QueryMap Map<String, String> map);

    /**
     * 获取仪表盘列表
     */
    @GET("instrumentPanel/findAll")
    Observable<ChartListResultBean> findAll();

    /**
     * 获取报表定义详情
     */
    @GET("report/getReportLayoutDetail")
    Observable<ReportDetailResultBean> getReportLayoutDetail(@Query("reportId") String reportId);

    /**
     * 获取筛选字段
     */
    @GET("report/getReportFilterFields")
    Observable<FilterFieldResultBean> getFilterFields(@Query("reportId") String reportId);



    /**
     * 获取报表数据详情
     */
    @Headers({"Content-Type: application/json"})
    @POST("report/getReportDataDetail")
    Observable<ReportDetailResultBean> getReportDataDetail(@Body Map<String,Object> map);
    /**
     * 获取报表数据详情
     */
    @GET("instrumentPanel/getLayout")
    Observable<ChartDataResultBean> getChartDataDetail(@Query("id") String id);

}
