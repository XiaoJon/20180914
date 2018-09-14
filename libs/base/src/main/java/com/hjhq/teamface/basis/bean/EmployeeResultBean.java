package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.database.Member;

import java.io.Serializable;
import java.util.List;

/**
 * Crm使用的 员工实体
 * Created by lx on 2017/9/30.
 */

public class EmployeeResultBean extends BaseBean implements Serializable {

    private List<Member> data;

    public List<Member> getData() {
        return data;
    }

    public void setData(List<Member> data) {
        this.data = data;
    }

}
