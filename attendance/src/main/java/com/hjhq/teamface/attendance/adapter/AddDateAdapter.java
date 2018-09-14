package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;

import java.util.List;


public class AddDateAdapter extends BaseQuickAdapter<AddDateBean, BaseViewHolder> {
    int type;

    public AddDateAdapter(int type, List<AddDateBean> data) {
        super(R.layout.attendance_add_date_item, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, AddDateBean item) {
        //日期
        helper.setText(R.id.tv1, DateTimeUtil.longToStr(TextUtil.parseLong(item.getTime()), "yyyy.MM.dd(EEEE)"));

        //打卡规则
        helper.setText(R.id.content, item.getName() + ":" + item.getLabel());
        if (type == 1) {
            helper.setVisible(R.id.rl11, true);
        } else if (type == 2) {
            helper.setVisible(R.id.rl11, false);
        }
        helper.addOnClickListener(R.id.delete);

    }


}