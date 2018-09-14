package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 * Describe：所有模块列表
 */

public class MolduleListBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * chinese_name : 供应商
         * data_auth : 2
         * id : 2
         * english_name : bean1517630335230
         */

        private String chinese_name;
        private String data_auth;
        private String id;
        private String english_name;

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }

        public String getData_auth() {
            return data_auth;
        }

        public void setData_auth(String data_auth) {
            this.data_auth = data_auth;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEnglish_name() {
            return english_name;
        }

        public void setEnglish_name(String english_name) {
            this.english_name = english_name;
        }
    }
}
