package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;

import java.util.List;


public class AttendanceLocationAdapter extends BaseQuickAdapter<AttendanceTypeListBean, BaseViewHolder> {

    public AttendanceLocationAdapter(List<AttendanceTypeListBean> data) {
        super(R.layout.attendance_location_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, AttendanceTypeListBean item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.address, item.getAddress());
        helper.setText(R.id.range, String.format(helper.getConvertView().getContext().getString(R.string.attendance_range_str), item.getEffective_range()));
        helper.addOnClickListener(R.id.delete);
    }
}