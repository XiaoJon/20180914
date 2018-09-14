package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class ConversationListBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * is_hide : 0
         * chat_type : 2//1群,2单聊,3自定义
         * update_time : 1512378124242
         * receiver_id : 5
         * top_status : 0
         * employee_name : 陈宇亮
         * id : 5
         * no_bother : 0
         * picture :I
         * notice  : 群公告
         * name : 群组名
         * peoples  : 1，2
         * create_time : 1432165464
         * principal : 1
         * type : 1//0公司总群,1普通群
         */

        private String is_hide;
        private int chat_type;
        private long update_time;
        private String receiver_id;
        private String top_status;
        private String employee_name;
        private String id;
        private String no_bother;
        //只查看未读,0否1是
        private String show_type;
        private String picture;
        private String notice;
        private String name;
        private String peoples;
        private String create_time;
        private String principal;
        private String type;
        private String application_id;
        private String unread_nums;
        private String latest_push_content;
        private String latest_push_time;
        String icon_color;
        String icon_type;
        String icon_url;

        public String getApplication_id() {
            return application_id;
        }

        public void setApplication_id(String application_id) {
            this.application_id = application_id;
        }

        public String getIcon_color() {
            return icon_color;
        }

        public void setIcon_color(String icon_color) {
            this.icon_color = icon_color;
        }

        public String getIcon_type() {
            return icon_type;
        }

        public void setIcon_type(String icon_type) {
            this.icon_type = icon_type;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getShow_type() {
            return show_type;
        }

        public void setShow_type(String show_type) {
            this.show_type = show_type;
        }

        public String getLatest_push_content() {
            return latest_push_content;
        }

        public void setLatest_push_content(String latest_push_content) {
            this.latest_push_content = latest_push_content;
        }

        public String getUnread_nums() {
            return unread_nums;
        }

        public void setUnread_nums(String unread_nums) {
            this.unread_nums = unread_nums;
        }

        public String getIs_hide() {
            return is_hide;
        }

        public void setIs_hide(String is_hide) {
            this.is_hide = is_hide;
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

        public String getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(String receiver_id) {
            this.receiver_id = receiver_id;
        }

        public String getTop_status() {
            return top_status;
        }

        public void setTop_status(String top_status) {
            this.top_status = top_status;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo_bother() {
            return no_bother;
        }

        public void setNo_bother(String no_bother) {
            this.no_bother = no_bother;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeoples() {
            return peoples;
        }

        public void setPeoples(String peoples) {
            this.peoples = peoples;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApplicationId() {
            return application_id;
        }

        public void setApplicationId(String applicationId) {
            this.application_id = applicationId;
        }

        public String getLatest_push_time() {
            return latest_push_time;
        }

        public void setLatest_push_time(String latest_push_time) {
            this.latest_push_time = latest_push_time;
        }
    }
}