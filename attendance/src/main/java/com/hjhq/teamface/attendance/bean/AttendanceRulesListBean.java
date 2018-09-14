package com.hjhq.teamface.attendance.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/28.
 * Describe：打卡规则列表
 */

public class AttendanceRulesListBean extends BaseBean {

    /**
     * data : {"dataList":[{"name":"考勤组1-1","id":19,"attendance_time":"星期一,星期二,星期三,星期四  班3-2:07:00-14:00;12:00-15:00;16:00-18:00;星期五  班3-2:07:00-14:00;12:00-15:00;16:00-18:00;;星期六,星期日  休息","attendance_number":0,"attendance_type":"0"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<AddDateBean> dataList;

        public List<AddDateBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<AddDateBean> dataList) {
            this.dataList = dataList;
        }

    }
}
