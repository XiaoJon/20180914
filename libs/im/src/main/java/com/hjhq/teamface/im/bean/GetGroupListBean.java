package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.util.TextUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author 群聊列表
 * @date 2017/6/3
 */

public class GetGroupListBean extends BaseBean implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * peoples : 47,37,38
         * principal : 38
         * is_hide : 0
         * chat_type : 1
         * update_time : 1512988902136
         * create_time : 1512988902136
         * name : Snared
         * top_status : 0
         * id : 77
         * no_bother : 0
         * type : 1
         * notice : 666
         */

        private String peoples;
        private String principal;
        private String is_hide;
        private String chat_type;
        private String update_time;
        private String create_time;
        private String name;
        private String top_status;
        private String id;
        private String no_bother;
        private String type;
        private String notice;

        public String getPeoples() {
            return peoples;
        }

        public void setPeoples(String peoples) {
            this.peoples = peoples;
        }

        public long getPrincipal() {
            return TextUtil.parseInt(principal);
        }

        public void setPrincipal(long principal) {
            this.principal = principal + "";
        }

        public String getIs_hide() {
            return is_hide;
        }

        public void setIs_hide(String is_hide) {
            this.is_hide = is_hide;
        }

        public int getChat_type() {
            return TextUtil.parseInt(chat_type);
        }

        public void setChat_type(int chat_type) {
            this.chat_type = chat_type + "";
        }

        public long getUpdate_time() {
            return TextUtil.parseLong(update_time);
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time + "";
        }

        public long getCreate_time() {
            return TextUtil.parseLong(create_time);
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time + "";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTop_status() {
            return top_status;
        }

        public void setTop_status(String top_status) {
            this.top_status = top_status;
        }

        public long getId() {
            return TextUtil.parseLong(id);
        }

        public void setId(long id) {
            this.id = id + "";
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

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }
}