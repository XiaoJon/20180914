package com.hjhq.teamface.project.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/26.
 */

public class PersonalTaskLikeBean implements Serializable {

    private String employee_id;
    private String employee_name;

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
}
