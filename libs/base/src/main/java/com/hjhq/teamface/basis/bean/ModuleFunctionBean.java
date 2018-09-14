package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * Describe：权限数据
 */

public class ModuleFunctionBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * module_id : 1
         * data_auth : 0
         * role_id : 1
         * func_id : 1
         * func_type : 0
         * id : 376
         * auth_code : 1
         * func_name : 新增/导入市场活动
         */

        private String module_id;
        private String data_auth;
        private String role_id;
        private String func_id;
        private String func_type;
        private String id;
        private String auth_code;
        private String func_name;

        public String getModule_id() {
            return module_id;
        }

        public void setModule_id(String module_id) {
            this.module_id = module_id;
        }

        public String getData_auth() {
            return data_auth;
        }

        public void setData_auth(String data_auth) {
            this.data_auth = data_auth;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }

        public String getFunc_id() {
            return func_id;
        }

        public void setFunc_id(String func_id) {
            this.func_id = func_id;
        }

        public String getFunc_type() {
            return func_type;
        }

        public void setFunc_type(String func_type) {
            this.func_type = func_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuth_code() {
            return auth_code;
        }

        public void setAuth_code(String auth_code) {
            this.auth_code = auth_code;
        }

        public String getFunc_name() {
            return func_name;
        }

        public void setFunc_name(String func_name) {
            this.func_name = func_name;
        }
    }
}