package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddDateBean;

import java.util.List;


public class WorkTimeManageListAdapter extends BaseQuickAdapter<AddDateBean, BaseViewHolder> {

    public WorkTimeManageListAdapter(List<AddDateBean> data) {
        super(R.layout.attendance_work_time_manage_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, AddDateBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_time, "考勤时间：" + item.getAttendance_time());
        helper.setText(R.id.tv_time, String.format(helper.getConvertView().getContext().getResources().getString(R.string.attendance_work_time_item_title), item.getAttendance_time()));
        helper.addOnClickListener(R.id.tv_delete);

    }

}