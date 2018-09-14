package com.hjhq.teamface.oa.main;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.componentservice.statistic.StatisticService;
import com.luojilab.component.componentlib.router.Router;

/**
 * 报表分析
 *
 * @author Administrator
 * @date 2018/4/27
 */

public class ReportActivity extends BaseTitleActivity {
    @Override
    protected int getChildView() {
        return R.layout.activity_report;
    }

    @Override
    protected void initData() {
        setActivityTitle(R.string.report);
        StatisticService service = (StatisticService) Router.getInstance().getService(StatisticService.class.getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.fl_layout, service.getReport()).commit();
    }

    @Override
    protected void setListener() {

    }


    @Override
    public void onClick(View view) {

    }


}
