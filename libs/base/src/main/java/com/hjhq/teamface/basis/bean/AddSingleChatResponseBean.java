package com.hjhq.teamface.basis.bean;


/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class AddSingleChatResponseBean extends BaseBean {

    /**
     * data : {"is_hide":"0","chat_type":2,"update_time":1512378124242,"receiver_id":5653654634563456345,"top_status":"0","employee_name":"陈宇亮","id":56345634563546345,"no_bother":"0","picture":""}
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
         * chat_type : 2
         * update_time : 1512378124242
         * receiver_id : 5653654634563456345
         * top_status : 0
         * employee_name : 陈宇亮
         * id : 56345634563546345
         * no_bother : 0
         * picture :
         */

        private String is_hide;
        private int chat_type;
        private long update_time;
        private String receiver_id;
        private String top_status;
        private String employee_name;
        private String id;
        private String no_bother;
        private String picture;

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
    }
}