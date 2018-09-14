package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class TaskListResultBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TaskLikeBean> dataList;

        public List<TaskLikeBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<TaskLikeBean> dataList) {
            this.dataList = dataList;
        }
    }
}
