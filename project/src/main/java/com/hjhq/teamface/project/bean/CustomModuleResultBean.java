package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class CustomModuleResultBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * data_auth : 2
         * name : 面试评估
         * moduleid : 3
         * bean : bean1529636406668
         */

        private String data_auth;
        private String name;
        private String moduleid;
        private String bean;

        public String getData_auth() {
            return data_auth;
        }

        public void setData_auth(String data_auth) {
            this.data_auth = data_auth;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModuleid() {
            return moduleid;
        }

        public void setModuleid(String moduleid) {
            this.moduleid = moduleid;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }
    }
}
