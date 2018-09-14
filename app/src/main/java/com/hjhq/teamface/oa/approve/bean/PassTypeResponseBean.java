package com.hjhq.teamface.oa.approve.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class PassTypeResponseBean extends BaseBean {

    /**
     * data : {"approvalFlag":1,"employeeList":[{"id":1,"employee_name":"曹建华","picture":"","leader":"0","phone":"18975520503","mobile_phone":"","email":"","status":"0","account":"","is_enable":"1","post_id":1,"role_id":1,"del_status":"0","microblog_background":"","sex":"0","sign":"","personnel_create_by":"","datetime_create_time":"","post_name":"产品总监"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * "processType":1,//0固定流程 1自由流程
         * approvalFlag : 1
         * employeeList : [{"id":1,"employee_name":"曹建华","picture":"","leader":"0","phone":"18975520503","mobile_phone":"","email":"","status":"0","account":"","is_enable":"1","post_id":1,"role_id":1,"del_status":"0","microblog_background":"","sex":"0","sign":"","personnel_create_by":"","datetime_create_time":"","post_name":"产品总监"}]
         */

        private String processType;
        private String approvalFlag;
        private List<EmployeeListBean> employeeList;

        public String getApprovalFlag() {
            return approvalFlag;
        }

        public void setApprovalFlag(String approvalFlag) {
            this.approvalFlag = approvalFlag;
        }

        public List<EmployeeListBean> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(List<EmployeeListBean> employeeList) {
            this.employeeList = employeeList;
        }

        public String getProcessType() {
            return processType;
        }

        public void setProcessType(String processType) {
            this.processType = processType;
        }

        public static class EmployeeListBean {
            /**
             * id : 1
             * employee_name : 曹建华
             * picture :
             * leader : 0
             * phone : 18975520503
             * mobile_phone :
             * email :
             * status : 0
             * account :
             * is_enable : 1
             * post_id : 1
             * role_id : 1
             * del_status : 0
             * microblog_background :
             * sex : 0
             * sign :
             * personnel_create_by :
             * datetime_create_time :
             * post_name : 产品总监
             */

            private String id;
            private String employee_name;
            private String picture;
            private String leader;
            private String phone;
            private String mobile_phone;
            private String email;
            private String status;
            private String account;
            private String is_enable;
            private String post_id;
            private String role_id;
            private String del_status;
            private String microblog_background;
            private String sex;
            private String sign;
            private String personnel_create_by;
            private String datetime_create_time;
            private String post_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getLeader() {
                return leader;
            }

            public void setLeader(String leader) {
                this.leader = leader;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getMobile_phone() {
                return mobile_phone;
            }

            public void setMobile_phone(String mobile_phone) {
                this.mobile_phone = mobile_phone;
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

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
            }

            public String getRole_id() {
                return role_id;
            }

            public void setRole_id(String role_id) {
                this.role_id = role_id;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getMicroblog_background() {
                return microblog_background;
            }

            public void setMicroblog_background(String microblog_background) {
                this.microblog_background = microblog_background;
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

            public String getPersonnel_create_by() {
                return personnel_create_by;
            }

            public void setPersonnel_create_by(String personnel_create_by) {
                this.personnel_create_by = personnel_create_by;
            }

            public String getDatetime_create_time() {
                return datetime_create_time;
            }

            public void setDatetime_create_time(String datetime_create_time) {
                this.datetime_create_time = datetime_create_time;
            }

            public String getPost_name() {
                return post_name;
            }

            public void setPost_name(String post_name) {
                this.post_name = post_name;
            }
        }
    }
}
