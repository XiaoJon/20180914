package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class GroupChatInfoBean extends BaseBean {

    /**
     * data : {"employeeInfo":[null,null,null,null,null,null,null,null,null],"groupInfo":{"peoples":"47,39,38,37,31,30,40,36,47","principal":47,"is_hide":"0","chat_type":1,"update_time":1512557073409,"create_time":1512557073409,"name":"群三","top_status":"0","id":42,"no_bother":"0","type":"1","notice":""}}
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
         * employeeInfo : [null,null,null,null,null,null,null,null,null]
         * groupInfo : {"peoples":"47,39,38,37,31,30,40,36,47","principal":47,"is_hide":"0","chat_type":1,"update_time":1512557073409,"create_time":1512557073409,"name":"群三","top_status":"0","id":42,"no_bother":"0","type":"1","notice":""}
         */

        private GroupInfoBean groupInfo;
        private List<EmployeeInfoBean> employeeInfo;

        public GroupInfoBean getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(GroupInfoBean groupInfo) {
            this.groupInfo = groupInfo;
        }

        public List<EmployeeInfoBean> getEmployeeInfo() {
            return employeeInfo;
        }

        public void setEmployeeInfo(List<EmployeeInfoBean> employeeInfo) {
            this.employeeInfo = employeeInfo;
        }

        public static class GroupInfoBean {

            /**
             * peoples : 36,47
             * is_hide : 0
             * create_time : 1512697167272
             * top_status : 1
             * principal_name : 阿栋
             * no_bother : 0
             * type : 1
             * principal : 47
             * chat_type : 1
             * update_time : 1512708573566
             * name : 哈哈哈哈
             * id : 56
             * notice : vvvv嘎嘎嘎嘎
             */

            private String peoples;
            private String is_hide;
            private long create_time;
            private String top_status;
            private String principal_name;
            private String no_bother;
            private String type;
            private long principal;
            private int chat_type;
            private long update_time;
            private String name;
            private long id;
            private String notice;

            public String getPeoples() {
                return peoples;
            }

            public void setPeoples(String peoples) {
                this.peoples = peoples;
            }

            public String getIs_hide() {
                return is_hide;
            }

            public void setIs_hide(String is_hide) {
                this.is_hide = is_hide;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getTop_status() {
                return top_status;
            }

            public void setTop_status(String top_status) {
                this.top_status = top_status;
            }

            public String getPrincipal_name() {
                return principal_name;
            }

            public void setPrincipal_name(String principal_name) {
                this.principal_name = principal_name;
            }

            public String getNo_bother() {
                return no_bother;
            }

            public void setNo_bother(String no_bother) {
                this.no_bother = no_bother;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getPrincipal() {
                return principal;
            }

            public void setPrincipal(long principal) {
                this.principal = principal;
            }

            public int getChat_type() {
                return chat_type;
            }

            public void setChat_type(int chat_type) {
                this.chat_type = chat_type;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }
        }

        public static class EmployeeInfoBean{

            /**
             * leader : 0
             * create_time :
             * sign_id : 365346546546456456459
             * is_enable : 0
             * sex :
             * sign :
             * employee_name : 尹明亮
             * picture :
             * microblog_background :
             * create_by :
             * post_id :
             * phone : 15818548636
             * role_id : 63465465654645
             * mobile_phone :
             * is_del : 0
             * id : 1654645645645640
             * email :
             * account : YML
             * status : 0
             */

            private String leader;
            private String create_time;
            private String sign_id;
            private String is_enable;
            private String sex;
            private String sign;
            private String employee_name;
            private String picture;
            private String microblog_background;
            private String create_by;
            private String post_id;
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

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getSign_id() {
                return sign_id;
            }

            public void setSign_id(String sign_id) {
                this.sign_id = sign_id;
            }

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
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

            public String getMicroblog_background() {
                return microblog_background;
            }

            public void setMicroblog_background(String microblog_background) {
                this.microblog_background = microblog_background;
            }

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
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

            public long getRole_id() {
                return role_id;
            }

            public void setRole_id(long role_id) {
                this.role_id = role_id;
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

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
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