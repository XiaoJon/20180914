package com.hjhq.teamface.statistic.serviceimpl;

import android.support.v4.app.Fragment;

import com.hjhq.teamface.componentservice.statistic.StatisticService;
import com.hjhq.teamface.statistic.ui.ChartTempFragment;
import com.hjhq.teamface.statistic.ui.ReportTempFragment;

/**
 * 自定义组件对外接口实现
 *
 * @author Administrator
 * @date 2018/3/26
 */

public class StatisticServiceImpl implements StatisticService {
    @Override
    public Fragment getReport() {
        return new ReportTempFragment();
    }

    @Override
    public Fragment getChart() {
        return new ChartTempFragment();
    }
}
