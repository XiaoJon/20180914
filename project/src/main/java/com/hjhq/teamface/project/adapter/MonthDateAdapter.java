package com.hjhq.teamface.project.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.MonthDateBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class MonthDateAdapter extends BaseQuickAdapter<MonthDateBean, BaseViewHolder> {
    public MonthDateAdapter(List<MonthDateBean> data) {
        super(R.layout.project_item_month_date, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MonthDateBean item) {

        helper.setText(R.id.tv_day, item.getDay() + "");
        helper.getView(R.id.tv_day).setSelected(item.isCheck());
    }
}
