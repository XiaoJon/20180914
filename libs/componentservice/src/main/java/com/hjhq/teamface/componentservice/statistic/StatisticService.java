package com.hjhq.teamface.componentservice.statistic;

import android.support.v4.app.Fragment;

/**
 * 统计
 * Created by Administrator on 2018/3/26.
 */

public interface StatisticService {
    /**
     * 得到报表
     */
    Fragment getReport();

    /**
     * 得到仪表盘
     */
    Fragment getChart();
}
