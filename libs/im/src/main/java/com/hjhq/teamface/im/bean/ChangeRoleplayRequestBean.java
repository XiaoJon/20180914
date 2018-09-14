package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 18:14 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class 角色类型0其他1所有者2管理员3成员4访客
 * @anthor Administrator TEL:
 * @time 2017/5/13 18:14
 * @change
 * @chang time
 * @class describe
 */
public class ChangeRoleplayRequestBean {

    /**
     * employeeIds : [3356812542001152]
     * roleType : 1
     * 角色类型0其他1所有者2管理员3成员4访客
     */

    private int roleType;
    private List<Long> employeeIds;

    public ChangeRoleplayRequestBean(int roleType, List<Long> employeeIds) {
        this.roleType = roleType;
        this.employeeIds = employeeIds;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
