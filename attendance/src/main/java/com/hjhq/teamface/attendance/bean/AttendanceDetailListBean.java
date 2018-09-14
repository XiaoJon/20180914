package com.hjhq.teamface.attendance.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 * Describe：
 */

public class AttendanceDetailListBean extends BaseBean {
    List<AttendanceDetailDataBean> data;

    public List<AttendanceDetailDataBean> getData() {
        return data;
    }

    public void setData(List<AttendanceDetailDataBean> data) {
        this.data = data;
    }
}
