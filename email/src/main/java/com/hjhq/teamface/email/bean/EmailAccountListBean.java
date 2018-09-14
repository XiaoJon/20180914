package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 审批权限bean
 * Created by Administrator on 2018/1/17.
 */

public class EmailAccountListBean extends BaseBean implements Serializable {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * send_server_port : 263
         * sended_sychronize : 0
         * sending_sychronize : 0
         * create_time : 1520318478659
         * receive_server_port : 45
         * starttls_transport_secure : 1
         * receive_server : pop3.126.com
         * send_server_secure : 0
         * receive_server_type : POP3
         * password : wkd2018
         * receive_server_secure : 0
         * employee_id : 1
         * nickname : 王克栋
         * id : 6
         * account_default : 0
         * account : wkd2018@126.com
         * send_server : smtp.126.com
         * status : 0
         */

        private String send_server_port;
        private String sended_sychronize;
        private String sending_sychronize;
        private String create_time;
        private String receive_server_port;
        private String starttls_transport_secure;
        private String receive_server;
        private String send_server_secure;
        private String receive_server_type;
        private String password;
        private String receive_server_secure;
        private String employee_id;
        private String nickname;
        private String id;
        private String account_default;
        private String account;
        private String send_server;
        private String status;
        boolean isDefault;
        boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public String getSend_server_port() {
            return send_server_port;
        }

        public void setSend_server_port(String send_server_port) {
            this.send_server_port = send_server_port;
        }

        public String getSended_sychronize() {
            return sended_sychronize;
        }

        public void setSended_sychronize(String sended_sychronize) {
            this.sended_sychronize = sended_sychronize;
        }

        public String getSending_sychronize() {
            return sending_sychronize;
        }

        public void setSending_sychronize(String sending_sychronize) {
            this.sending_sychronize = sending_sychronize;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getReceive_server_port() {
            return receive_server_port;
        }

        public void setReceive_server_port(String receive_server_port) {
            this.receive_server_port = receive_server_port;
        }

        public String getStarttls_transport_secure() {
            return starttls_transport_secure;
        }

        public void setStarttls_transport_secure(String starttls_transport_secure) {
            this.starttls_transport_secure = starttls_transport_secure;
        }

        public String getReceive_server() {
            return receive_server;
        }

        public void setReceive_server(String receive_server) {
            this.receive_server = receive_server;
        }

        public String getSend_server_secure() {
            return send_server_secure;
        }

        public void setSend_server_secure(String send_server_secure) {
            this.send_server_secure = send_server_secure;
        }

        public String getReceive_server_type() {
            return receive_server_type;
        }

        public void setReceive_server_type(String receive_server_type) {
            this.receive_server_type = receive_server_type;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getReceive_server_secure() {
            return receive_server_secure;
        }

        public void setReceive_server_secure(String receive_server_secure) {
            this.receive_server_secure = receive_server_secure;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount_default() {
            return account_default;
        }

        public void setAccount_default(String account_default) {
            this.account_default = account_default;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSend_server() {
            return send_server;
        }

        public void setSend_server(String send_server) {
            this.send_server = send_server;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
