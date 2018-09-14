package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;

import java.util.List;


public class AttendanceWifiAdapter extends BaseQuickAdapter<AttendanceTypeListBean, BaseViewHolder> {

    public AttendanceWifiAdapter(List<AttendanceTypeListBean> data) {
        super(R.layout.attendance_wifi_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttendanceTypeListBean item) {
        helper.setText(R.id.ssid, item.getName());
        helper.setText(R.id.bssid, item.getAddress());
        helper.addOnClickListener(R.id.delete);
    }
}