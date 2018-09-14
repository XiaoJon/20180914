package com.hjhq.teamface.oa.main;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.componentservice.statistic.StatisticService;
import com.hjhq.teamface.util.CommonUtil;
import com.luojilab.component.componentlib.router.Router;

import butterknife.Bind;

/**
 * 数据分析
 *
 * @author Administrator
 * @date 2018/4/27
 */

public class StatisticFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_report)
    ImageView ivReport;
    @Bind(R.id.ll_title)
    View llTitle;

    @Override
    protected int getContentView() {
        return R.layout.fragment_statistics;
    }


    @Override
    protected void initView(View view) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        llTitle.setPadding(0, statusBarHeight, 0, 0);
    }

    @Override
    protected void initData() {
        StatisticService service = (StatisticService) Router.getInstance().getService(StatisticService.class.getSimpleName());
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_layout, service.getChart()).commit();
    }

    @Override
    protected void setListener() {
        setOnClicks(ivReport);
    }

    @Override
    public void onClick(View v) {
        CommonUtil.startActivtiy(mContext, ReportActivity.class);
    }

}
