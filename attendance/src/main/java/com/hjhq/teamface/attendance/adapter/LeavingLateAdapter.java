package com.hjhq.teamface.attendance.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddLeaveingLateRulesBean;
import com.hjhq.teamface.basis.util.NumberFormatUtil;

import java.util.List;


public class LeavingLateAdapter extends BaseQuickAdapter<AddLeaveingLateRulesBean, BaseViewHolder> {

    public LeavingLateAdapter(List<AddLeaveingLateRulesBean> data) {
        super(R.layout.attendance_additional_settings_type1_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, AddLeaveingLateRulesBean item) {
        helper.setText(R.id.title,
                String.format(helper.getConvertView().getContext().getString(R.string.attendance_rules_label), NumberFormatUtil.integerToString(helper.getAdapterPosition() + 1)));
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.set);
        helper.setText(R.id.text1, String.format(helper.getConvertView().getContext().getString(R.string.attendance_leave_time), item.getLeaveingLateTime()));
        helper.setText(R.id.text2, String.format(helper.getConvertView().getContext().getString(R.string.attendance_arrival_time), item.getWorkLateTime()));
        helper.setText(R.id.desc, String.format(helper.getConvertView().getContext().getString(R.string.attendance_leave_arrival_time), item.getLeaveingLateTime(), item.getWorkLateTime()));
        // helper.setText(R.id.text1, format(helper.getConvertView().getContext(), R.string.attendance_leave_time, item.getLeaveingLateTime()));
    }

    public String format(Context context, int stringRes, int... value) {
        return String.format(context.getString(stringRes), value);

    }

}