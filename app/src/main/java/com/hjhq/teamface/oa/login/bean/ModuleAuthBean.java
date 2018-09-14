package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * Describe：
 */

public class ModuleAuthBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * module_bean : activity
         * create_time : 1508919531205
         * module_type : 0
         * modify_time : 1508919531205
         * modify_by : 1
         * module_buy_status : 1
         * module_icon : icon_activity
         * module_special_type : 1
         * create_by : 1
         * data_auth : 0
         * module_label : 市场活动
         * module_enable_status : 1
         * parent_id : 1
         * id : 1
         */

        private String module_bean;
        private long create_time;
        private int module_type;
        private long modify_time;
        private String modify_by;
        private int module_buy_status;
        private String module_icon;
        private int module_special_type;
        private String create_by;
        private int data_auth;
        private String module_label;
        private int module_enable_status;
        private String parent_id;
        private String id;

        public String getModule_bean() {
            return module_bean;
        }

        public void setModule_bean(String module_bean) {
            this.module_bean = module_bean;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getModule_type() {
            return module_type;
        }

        public void setModule_type(int module_type) {
            this.module_type = module_type;
        }

        public long getModify_time() {
            return modify_time;
        }

        public void setModify_time(long modify_time) {
            this.modify_time = modify_time;
        }

        public String getModify_by() {
            return modify_by;
        }

        public void setModify_by(String modify_by) {
            this.modify_by = modify_by;
        }

        public int getModule_buy_status() {
            return module_buy_status;
        }

        public void setModule_buy_status(int module_buy_status) {
            this.module_buy_status = module_buy_status;
        }

        public String getModule_icon() {
            return module_icon;
        }

        public void setModule_icon(String module_icon) {
            this.module_icon = module_icon;
        }

        public int getModule_special_type() {
            return module_special_type;
        }

        public void setModule_special_type(int module_special_type) {
            this.module_special_type = module_special_type;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public int getData_auth() {
            return data_auth;
        }

        public void setData_auth(int data_auth) {
            this.data_auth = data_auth;
        }

        public String getModule_label() {
            return module_label;
        }

        public void setModule_label(String module_label) {
            this.module_label = module_label;
        }

        public int getModule_enable_status() {
            return module_enable_status;
        }

        public void setModule_enable_status(int module_enable_status) {
            this.module_enable_status = module_enable_status;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}