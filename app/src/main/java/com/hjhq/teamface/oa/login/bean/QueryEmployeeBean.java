package com.hjhq.teamface.oa.login.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：查找员工
 */

public class QueryEmployeeBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * leader : 0
         * mood :
         * is_enable : 1
         * personnel_create_by :
         * sex :
         * sign :
         * birth :
         * employee_name : 莫凡-开发管理
         * del_status : 0
         * card_template :
         * hide_set :
         * picture :
         * microblog_background :
         * datetime_create_time :
         * background_picture :
         * post_id :
         * phone : 15011111111
         * role_id : 5
         * mobile_phone :
         * id : 7
         * region :
         * email :
         * account :
         * status : 0
         */

        private String leader;
        private String mood;
        private String is_enable;
        private String personnel_create_by;
        private String sex;
        private String sign;
        private String birth;
        private String employee_name;
        private String del_status;
        private String card_template;
        private String hide_set;
        private String picture;
        private String microblog_background;
        private String datetime_create_time;
        private String background_picture;
        private String post_id;
        private String phone;
        private String role_id;
        private String mobile_phone;
        private String id;
        private String region;
        private String email;
        private String account;
        private String status;
        private String sign_id;

        public String getSign_id() {
            return sign_id;
        }

        public void setSign_id(String sign_id) {
            this.sign_id = sign_id;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getMood() {
            return mood;
        }

        public void setMood(String mood) {
            this.mood = mood;
        }

        public String getIs_enable() {
            return is_enable;
        }

        public void setIs_enable(String is_enable) {
            this.is_enable = is_enable;
        }

        public String getPersonnel_create_by() {
            return personnel_create_by;
        }

        public void setPersonnel_create_by(String personnel_create_by) {
            this.personnel_create_by = personnel_create_by;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getCard_template() {
            return card_template;
        }

        public void setCard_template(String card_template) {
            this.card_template = card_template;
        }

        public String getHide_set() {
            return hide_set;
        }

        public void setHide_set(String hide_set) {
            this.hide_set = hide_set;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getMicroblog_background() {
            return microblog_background;
        }

        public void setMicroblog_background(String microblog_background) {
            this.microblog_background = microblog_background;
        }

        public String getDatetime_create_time() {
            return datetime_create_time;
        }

        public void setDatetime_create_time(String datetime_create_time) {
            this.datetime_create_time = datetime_create_time;
        }

        public String getBackground_picture() {
            return background_picture;
        }

        public void setBackground_picture(String background_picture) {
            this.background_picture = background_picture;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
