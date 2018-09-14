package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/27.
 * Describe：
 */

public class MemoListItemBean implements Serializable {

    /**
     * share_ids :
     * createObj : {"leader":"0","mood":"","is_enable":"1","personnel_create_by":"","sex":"","sign":"","birth":"","employee_name":"默默","del_status":"0","remark":"","role_group_id":1,"picture":"http://192.168.1.9:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=company/1515374858725.timg.jpg","microblog_background":"","datetime_create_time":"","post_id":1,"phone":"13823690977","role_id":1,"mobile_phone":"","name":"企业所有者","id":1,"region":"","email":"","account":"","status":"0"}
     * items_arr :
     * create_time : 1522112623867
     * remind_time : 1522066195891
     * id : 31
     * title : 我鼓风机房刷卡缴费开发的管理地方
     * pic_url :
     */

    private String share_ids;
    private CreateObjBean createObj;
    private ArrayList<ModuleItemBean> items_arr;
    private String create_time;
    private String remind_time;
    private String id;
    private String title;
    private String pic_url;
    private boolean checked = false;
    private String create_by;

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public boolean isChecked() {

        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getShare_ids() {
        return share_ids;
    }

    public void setShare_ids(String share_ids) {
        this.share_ids = share_ids;
    }

    public CreateObjBean getCreateObj() {
        return createObj;
    }

    public void setCreateObj(CreateObjBean createObj) {
        this.createObj = createObj;
    }

    public ArrayList<ModuleItemBean> getItems_arr() {
        return items_arr;
    }

    public void setItems_arr(ArrayList<ModuleItemBean> items_arr) {
        this.items_arr = items_arr;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(String remind_time) {
        this.remind_time = remind_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public static class CreateObjBean implements Serializable {
        /**
         * leader : 0
         * mood :
         * is_enable : 1
         * personnel_create_by :
         * sex :
         * sign :
         * birth :
         * employee_name : 默默
         * del_status : 0
         * remark :
         * role_group_id : 1
         * picture : http://192.168.1.9:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=company/1515374858725.timg.jpg
         * microblog_background :
         * datetime_create_time :
         * post_id : 1
         * phone : 13823690977
         * role_id : 1
         * mobile_phone :
         * name : 企业所有者
         * id : 1
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
        private String remark;
        private String role_group_id;
        private String picture;
        private String microblog_background;
        private String datetime_create_time;
        private String post_id;
        private String phone;
        private String role_id;
        private String mobile_phone;
        private String name;
        private String id;
        private String region;
        private String email;
        private String account;
        private String status;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRole_group_id() {
            return role_group_id;
        }

        public void setRole_group_id(String role_group_id) {
            this.role_group_id = role_group_id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
