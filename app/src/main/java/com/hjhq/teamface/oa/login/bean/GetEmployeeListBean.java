package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class GetEmployeeListBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * leader : 1
         * post_name : 1
         * department_id : 1
         * phone : 1
         * user_name : 周杰伦
         * mobile_phone : 1
         * id : 4545243544434325443
         * picture : 1
         * email : 1
         * status : 1
         */

        private String leader;
        private String post_name;
        private long department_id;
        private String phone;
        private String user_name;
        private String mobile_phone;
        private long id;
        private String picture;
        private String email;
        private String status;

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getPost_name() {
            return post_name;
        }

        public void setPost_name(String post_name) {
            this.post_name = post_name;
        }

        public long getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(long department_id) {
            this.department_id = department_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
