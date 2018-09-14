package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class SubListResultBean extends BaseBean {

    /**
     * data : {"dataList":[]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<SubTaskBean> dataList;

        public List<SubTaskBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<SubTaskBean> dataList) {
            this.dataList = dataList;
        }
    }
}
