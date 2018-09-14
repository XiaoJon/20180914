package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddDateBean;

import java.util.List;


public class AttendanceRulesAdapter extends BaseQuickAdapter<AddDateBean, BaseViewHolder> {

    public AttendanceRulesAdapter(List<AddDateBean> data) {
        super(R.layout.attendance_rules_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, AddDateBean item) {
        helper.addOnClickListener(R.id.edit_members);
        helper.addOnClickListener(R.id.edit_rules);
        helper.addOnClickListener(R.id.delete);
        helper.setText(R.id.text1, item.getName());
        helper.setText(R.id.text2, "考勤人数:" + item.getAttendance_number());
        switch (item.getAttendance_type()) {
            case "0":
                helper.setText(R.id.text3, "考勤类型:");
                break;
            case "1":
                helper.setText(R.id.text3, "考勤类型:");
                break;
            case "2":
                helper.setText(R.id.text3, "考勤类型:" );
                break;
            default:
                helper.setText(R.id.text3, "考勤类型:");
                break;
        }

        helper.setText(R.id.text4, "考勤时间:" + item.getAttendance_time());

    }
}