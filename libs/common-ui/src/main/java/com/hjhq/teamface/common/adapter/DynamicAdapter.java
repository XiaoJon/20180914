package com.hjhq.teamface.common.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.bean.DynamicListResultBean;

import java.util.List;

/**
 * 动态适配器
 * Created by lx on 2017/4/10.
 */

public class DynamicAdapter extends BaseQuickAdapter<DynamicListResultBean.DataBean.TimeListBean, BaseViewHolder> {

    public DynamicAdapter(List<DynamicListResultBean.DataBean.TimeListBean> data) {
        super(R.layout.item_crm_pool_client_dynamic, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, DynamicListResultBean.DataBean.TimeListBean item) {
        //日期
        long timeDate = TextUtil.parseLong(item.getTimeDate());
        if (timeDate != 0) {
            String date = DateTimeUtil.longToStr(timeDate, "yyyy年MM月dd日");
            helper.setVisible(R.id.rl_date, true);
            helper.setText(R.id.tv_date, date);
        } else {
            helper.setVisible(R.id.rl_date, false);
        }

        //时间
        long time = TextUtil.parseLong(item.getDatetime_time());
        if (time != 0) {
            String date = DateTimeUtil.longToStr(time, "HH:mm");
            helper.setText(R.id.tv_time, date);
        }

        //人员
        helper.setText(R.id.tv_name, item.getEmployee_name());
        //内容
        helper.setText(R.id.tv_content, item.getContent());
    }
}
