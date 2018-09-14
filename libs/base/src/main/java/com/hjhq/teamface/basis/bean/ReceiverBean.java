package com.hjhq.teamface.basis.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/8.
 * Describe：
 */

public class ReceiverBean implements Serializable {
    /**
     * mail_account : dengshunli12345678@qq.com
     * employee_name : 张三
     */

    private String mail_account;
    private String employee_name;

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

}
