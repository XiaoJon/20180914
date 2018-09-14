package com.hjhq.teamface.attendance.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 * Describe：
 */

public class WorkTimeListBean extends BaseBean {

    /**
     * data : {"dataList":[{"name":"王克栋班次4","id":7,"attendance_time":"09:00-12.30;14:00-18:30;20:00-22:00;"}]}
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
