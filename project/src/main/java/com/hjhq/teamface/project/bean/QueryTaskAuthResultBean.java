package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class QueryTaskAuthResultBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * auth_10 : 1
         * auth_11 : 1
         * role_type : 1
         * create_time : 1529639484415
         * modify_time :
         * del_status : 0
         * modify_by :
         * auth_8 : 1
         * auth_7 : 1
         * auth_12 : 1
         * auth_9 : 1
         * auth_13 : 1
         * auth_4 : 1
         * create_by : 2
         * auth_3 : 1
         * auth_6 : 1
         * auth_5 : 1
         * project_id : 51
         * auth_2 : 1
         * auth_1 : 1
         * id : 137
         * role_type_describe :
         */

        private String auth_10;
        private String auth_11;
        private String auth_8;
        private String auth_7;
        private String auth_12;
        private String auth_9;
        private String auth_13;
        private String auth_4;
        private String auth_3;
        private String auth_6;
        private String auth_5;
        private String auth_2;
        private String auth_1;
        private String role_type;
        private long create_by;
        private long id;
        private long project_id;
        private String role_type_describe;

        private boolean isCheck;

        public String getAuth_10() {
            return auth_10;
        }

        public void setAuth_10(String auth_10) {
            this.auth_10 = auth_10;
        }

        public String getAuth_11() {
            return auth_11;
        }

        public void setAuth_11(String auth_11) {
            this.auth_11 = auth_11;
        }

        public String getRole_type() {
            return role_type;
        }

        public void setRole_type(String role_type) {
            this.role_type = role_type;
        }

        public String getAuth_8() {
            return auth_8;
        }

        public void setAuth_8(String auth_8) {
            this.auth_8 = auth_8;
        }

        public String getAuth_7() {
            return auth_7;
        }

        public void setAuth_7(String auth_7) {
            this.auth_7 = auth_7;
        }

        public String getAuth_12() {
            return auth_12;
        }

        public void setAuth_12(String auth_12) {
            this.auth_12 = auth_12;
        }

        public String getAuth_9() {
            return auth_9;
        }

        public void setAuth_9(String auth_9) {
            this.auth_9 = auth_9;
        }

        public String getAuth_13() {
            return auth_13;
        }

        public void setAuth_13(String auth_13) {
            this.auth_13 = auth_13;
        }

        public String getAuth_4() {
            return auth_4;
        }

        public void setAuth_4(String auth_4) {
            this.auth_4 = auth_4;
        }

        public long getCreate_by() {
            return create_by;
        }

        public void setCreate_by(long create_by) {
            this.create_by = create_by;
        }

        public String getAuth_3() {
            return auth_3;
        }

        public void setAuth_3(String auth_3) {
            this.auth_3 = auth_3;
        }

        public String getAuth_6() {
            return auth_6;
        }

        public void setAuth_6(String auth_6) {
            this.auth_6 = auth_6;
        }

        public String getAuth_5() {
            return auth_5;
        }

        public void setAuth_5(String auth_5) {
            this.auth_5 = auth_5;
        }

        public long getProject_id() {
            return project_id;
        }

        public void setProject_id(long project_id) {
            this.project_id = project_id;
        }

        public String getAuth_2() {
            return auth_2;
        }

        public void setAuth_2(String auth_2) {
            this.auth_2 = auth_2;
        }

        public String getAuth_1() {
            return auth_1;
        }

        public void setAuth_1(String auth_1) {
            this.auth_1 = auth_1;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getRole_type_describe() {
            return role_type_describe;
        }

        public void setRole_type_describe(String role_type_describe) {
            this.role_type_describe = role_type_describe;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
