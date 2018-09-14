package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.ArrayList;

/**
 * 全部节点
 * Created by Administrator on 2018/4/25.
 */

public class AllNodeResultBean extends BaseBean {


    /**
     * data : {"dataList":[{"create_by":"","subnodeArr":[{"create_by":"","create_time":"","modify_time":"","name":"待处理","main_id":7,"del_status":"0","id":25,"sort":1,"modify_by":""},{"create_by":"","create_time":"","modify_time":"","name":"进行中","main_id":7,"del_status":"0","id":26,"sort":2,"modify_by":""},{"create_by":"","create_time":"","modify_time":"","name":"已完成","main_id":7,"del_status":"0","id":27,"sort":3,"modify_by":""},{"create_by":"","create_time":"","modify_time":"","name":"归档","main_id":7,"del_status":"0","id":28,"sort":4,"modify_by":""}],"temp_status":"0","create_time":"","project_id":7,"temp_id":"","modify_time":"","name":"项目流程","del_status":"0","id":7,"sort":1,"modify_by":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private ArrayList<NodeBean> dataList;

        public ArrayList<NodeBean> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<NodeBean> dataList) {
            this.dataList = dataList;
        }
    }
}
