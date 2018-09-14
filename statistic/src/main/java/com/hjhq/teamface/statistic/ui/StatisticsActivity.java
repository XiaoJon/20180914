package com.hjhq.teamface.statistic.ui;


import android.support.v4.app.Fragment;

import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;

/**
 * 统计列表控制器
 *
 * @author lx
 * @date 2017/8/31
 */

@RouteNode(path = "/stat", desc = "统计")
public class StatisticsActivity extends ActivityPresenter<StatisticsDelegate, StatisticsModel> {
    private ArrayList<Fragment> fragmentList;

    private String[] fragmentTitles = new String[2];

    @Override
    public void init() {
        fragmentList = new ArrayList<>(2);
        fragmentList.add(new ChartTempFragment());
        fragmentList.add(new ReportTempFragment());
        fragmentTitles[0] = "仪表盘";
        fragmentTitles[1] = "报表";
        viewDelegate.initNavigator(fragmentTitles, fragmentList);
    }

}
