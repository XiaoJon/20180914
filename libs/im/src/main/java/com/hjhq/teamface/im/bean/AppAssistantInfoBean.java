package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * 小助手信息
 */
public class AppAssistantInfoBean extends BaseBean {

    /**
     * data : {"is_hide":"0","chat_type":3,"show_type":"0","update_time":1514539722220,"create_time":151233214312345,"name":"企信小助手","top_status":"0","id":1144444444444,"no_bother":"0","type":2,"application_id":""}
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
         * is_hide : 0
         * chat_type : 3
         * show_type : 0
         * update_time : 1514539722220
         * create_time : 151233214312345
         * name : 企信小助手
         * top_status : 0
         * id : 1144444444444
         * no_bother : 0
         * type : 2
         * application_id :
         */

        private String is_hide;
        private int chat_type;
        private String show_type;
        private long update_time;
        private long create_time;
        private String name;
        private String top_status;
        private long id;
        private String no_bother;
        private int type;
        private String application_id;

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

        public String getShow_type() {
            return show_type;
        }

        public void setShow_type(String show_type) {
            this.show_type = show_type;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
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
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getNo_bother() {
            return no_bother;
        }

        public void setNo_bother(String no_bother) {
            this.no_bother = no_bother;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getApplication_id() {
            return application_id;
        }

        public void setApplication_id(String application_id) {
            this.application_id = application_id;
        }
    }
}
