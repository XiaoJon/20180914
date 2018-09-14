package com.hjhq.teamface.statistic.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.bean.ReportListResultBean;

import java.util.List;

/**
 * 报表适配器
 *
 * @author Administrator
 * @date 2018/3/6
 */

public class ReportAdapter extends BaseQuickAdapter<ReportListResultBean.DataBean.ListBean, BaseViewHolder> {
    public ReportAdapter(List<ReportListResultBean.DataBean.ListBean> data) {
        super(R.layout.statistic_report_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportListResultBean.DataBean.ListBean item) {
        helper.setText(R.id.tv_report_label, item.getReport_label());
        helper.setText(R.id.tv_creater, "创建人: " + item.getCreate_by_name());
        helper.setText(R.id.tv_data_source_label, "数据源: " + item.getData_source_label());
    }
}
