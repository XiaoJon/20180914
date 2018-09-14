package com.hjhq.teamface.attendance.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/22.
 * Describe：
 */

public class AttendanceTypeBean extends BaseBean {

    /**
     * data : {"dataList":[{"create_by":1,"effective_range":"100","address":"广东省深圳市南山区科技园高新南一道21-2号","create_time":1529650771591,"modify_time":"","name":"公司2","location":[{"address":"广东省深圳市南山区科技园高新南一道21-2号","lng":113.946318,"lat":22.538232}],"del_status":"0","id":13,"modify_by":"","attendance_status":"0","attendance_type":"0"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<AttendanceTypeListBean> dataList;

        public List<AttendanceTypeListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<AttendanceTypeListBean> dataList) {
            this.dataList = dataList;
        }


    }
}
