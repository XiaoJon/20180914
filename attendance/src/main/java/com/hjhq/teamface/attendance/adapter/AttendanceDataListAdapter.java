package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;

import java.util.List;


public class AttendanceDataListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public AttendanceDataListAdapter(List<String> data) {
        super(R.layout.attendance_data_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text1, item);
        helper.setText(R.id.text2, helper.getAdapterPosition() + 1 + "");

    }
}