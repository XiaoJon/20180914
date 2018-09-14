package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 成员列表返回实体
 *
 * @author Administrator
 * @date 2017/10/24
 */

public class MemberResultBean extends BaseBean {


    /**
     * data : {"principal":{"leader":"0","is_enable":"0","employee_name":"李萌","picture":"","post_id":15,"phone":"17198669671","role_id":3,"mobile_phone":"","is_del":"0","id":25,"email":"","account":"008","status":"0"},"participant":[{"leader":"0","is_enable":"0","employee_name":"李萌","picture":"","post_id":15,"phone":"17198669671","role_id":3,"mobile_phone":"","is_del":"0","id":25,"email":"","account":"008","status":"0"},{"leader":"0","is_enable":"0","employee_name":"曹建华","picture":"http://teamface.oss-cn-shenzhen.aliyuncs.com/teamface/1503746972562/magazine-unlock-01-2.3.716-_0476943401944fb397f4f0ad5c021eca.jpg?Expires=33039746973&OSSAccessKeyId=LTAITSghA9rHy6Jv&Signature=QFcGAdXS3vvBngUm/T9tVVjWGOE%3D","post_id":15,"phone":"18975520503","role_id":2,"mobile_phone":"","is_del":"0","id":28,"email":"","account":"caojianhua","status":"0"}]}
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
         * principal : {"leader":"0","is_enable":"0","employee_name":"李萌","picture":"","post_id":15,"phone":"17198669671","role_id":3,"mobile_phone":"","is_del":"0","id":25,"email":"","account":"008","status":"0"}
         * participant : [{"leader":"0","is_enable":"0","employee_name":"李萌","picture":"","post_id":15,"phone":"17198669671","role_id":3,"mobile_phone":"","is_del":"0","id":25,"email":"","account":"008","status":"0"},{"leader":"0","is_enable":"0","employee_name":"曹建华","picture":"http://teamface.oss-cn-shenzhen.aliyuncs.com/teamface/1503746972562/magazine-unlock-01-2.3.716-_0476943401944fb397f4f0ad5c021eca.jpg?Expires=33039746973&OSSAccessKeyId=LTAITSghA9rHy6Jv&Signature=QFcGAdXS3vvBngUm/T9tVVjWGOE%3D","post_id":15,"phone":"18975520503","role_id":2,"mobile_phone":"","is_del":"0","id":28,"email":"","account":"caojianhua","status":"0"}]
         */

        private PrincipalBean principal;
        private List<PrincipalBean> participant;

        public PrincipalBean getPrincipal() {
            return principal;
        }

        public void setPrincipal(PrincipalBean principal) {
            this.principal = principal;
        }

        public List<PrincipalBean> getParticipant() {
            return participant;
        }

        public void setParticipant(List<PrincipalBean> participant) {
            this.participant = participant;
        }

        public static class PrincipalBean implements Serializable{
            /**
             * leader : 0
             * is_enable : 0
             * employee_name : 李萌
             * picture :
             * post_id : 15
             * phone : 17198669671
             * role_id : 3
             * mobile_phone :
             * is_del : 0
             * id : 25
             * email :
             * account : 008
             * status : 0
             */

            private String leader;
            private String is_enable;
            private String employee_name;
            private String picture;
            private long post_id;
            private String post_name;
            private String phone;
            private long role_id;
            private String mobile_phone;
            private String is_del;
            private long id;
            private String email;
            private String account;
            private String status;

            public String getLeader() {
                return leader;
            }

            public void setLeader(String leader) {
                this.leader = leader;
            }

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getPost_name() {
                return post_name;
            }

            public void setPost_name(String post_name) {
                this.post_name = post_name;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }


            public long getPost_id() {
                return post_id;
            }

            public void setPost_id(long post_id) {
                this.post_id = post_id;
            }

            public long getRole_id() {
                return role_id;
            }

            public void setRole_id(long role_id) {
                this.role_id = role_id;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
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

            public String getIs_del() {
                return is_del;
            }

            public void setIs_del(String is_del) {
                this.is_del = is_del;
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
}
