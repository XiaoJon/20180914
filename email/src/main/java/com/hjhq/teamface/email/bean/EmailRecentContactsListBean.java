package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 审批权限bean
 * Created by Administrator on 2018/1/17.
 */

public class EmailRecentContactsListBean extends BaseBean implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * create_time : 1520247532361
         * employee_id : 1
         * mail_account : snbumq@163.com
         * employee_name : 张三
         * id : 6
         */

        private long create_time;
        private String employee_id;
        private String mail_account;
        private String employee_name;
        private String id;

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getMail_account() {
            return mail_account;
        }

        public void setMail_account(String mail_account) {
            this.mail_account = mail_account;
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
    }
}
