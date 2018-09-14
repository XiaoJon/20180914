package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AttendanceDataBean;

import java.util.List;


public class AttendanceApprovalAdapter extends BaseQuickAdapter<AttendanceDataBean, BaseViewHolder> {

    public AttendanceApprovalAdapter(List<AttendanceDataBean> data) {
        super(R.layout.attendance_approval_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, AttendanceDataBean item) {
        helper.addOnClickListener(R.id.edit);
        helper.addOnClickListener(R.id.delete);

    }
}