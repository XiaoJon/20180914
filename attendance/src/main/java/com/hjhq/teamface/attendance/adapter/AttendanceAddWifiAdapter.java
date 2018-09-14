package com.hjhq.teamface.attendance.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AttendanceAddWifiBean;

import java.util.List;


public class AttendanceAddWifiAdapter extends BaseQuickAdapter<AttendanceAddWifiBean, BaseViewHolder> {
    String BSSID;

    public AttendanceAddWifiAdapter(List<AttendanceAddWifiBean> data) {
        super(R.layout.attendance_add_wifi_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, AttendanceAddWifiBean item) {
        //桥接的AP?
        //item.isPasspointNetwork();
        helper.setText(R.id.text1, item.getName());
        helper.setText(R.id.text2, "MAC地址：" + item.getAddress());
        //加密方式
        //helper.setText(R.id.text3, item.capabilities);
        if (item.isCurrentWifi()) {
            helper.setVisible(R.id.text3, true);
        } else {
            helper.setVisible(R.id.text3, false);
        }
        if (item.isCheck()) {
            helper.setVisible(R.id.check, true);
        } else {
            helper.setVisible(R.id.check, false);
        }

    }

    public void setBSSID(String bssid) {
        BSSID = bssid;

    }
}