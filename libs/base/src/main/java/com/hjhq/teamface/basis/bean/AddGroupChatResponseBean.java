package com.hjhq.teamface.basis.bean;


import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class AddGroupChatResponseBean extends BaseBean {

    /**
     * data : {"employeeInfo":[null,null],"groupInfo":{"peoples":"47,47","principal":47,"is_hide":"0","chat_type":1,"update_time":1512555819990,"create_time":1512555819990,"name":"v刚刚好","top_status":"0","id":40,"no_bother":"0","type":"1","notice":"腹股沟管"}}
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
         * employeeInfo : [null,null]
         * groupInfo : {"peoples":"47,47","principal":47,"is_hide":"0","chat_type":1,"update_time":1512555819990,"create_time":1512555819990,"name":"v刚刚好","top_status":"0","id":40,"no_bother":"0","type":"1","notice":"腹股沟管"}
         */

        private GroupInfoBean groupInfo;
        private List<Member> employeeInfo;

        public GroupInfoBean getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(GroupInfoBean groupInfo) {
            this.groupInfo = groupInfo;
        }

        public List<Member> getEmployeeInfo() {
            return employeeInfo;
        }

        public void setEmployeeInfo(List<Member> employeeInfo) {
            this.employeeInfo = employeeInfo;
        }

        public static class GroupInfoBean {
            /**
             * peoples : 47,47
             * principal : 47
             * is_hide : 0
             * chat_type : 1
             * update_time : 1512555819990
             * create_time : 1512555819990
             * name : v刚刚好
             * top_status : 0
             * id : 40
             * no_bother : 0
             * type : 1
             * notice : 腹股沟管
             */

            private String peoples;
            private String principal;
            private String is_hide;
            private int chat_type;
            private long update_time;
            private long create_time;
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

            public String getPrincipal() {
                return principal;
            }

            public void setPrincipal(String principal) {
                this.principal = principal;
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
}