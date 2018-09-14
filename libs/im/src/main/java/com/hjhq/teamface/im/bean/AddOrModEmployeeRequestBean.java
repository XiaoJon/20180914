package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/14 16:32 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/14 16:32
 * @change
 * @chang time
 * @class describe
 */
public class AddOrModEmployeeRequestBean {

    /**
     * id : 3328405891743744
     * employeeName : 你dsssssssssssss
     * telephone : 15888880000
     * employeeNumber : hq001
     * departmentIds : [3328280920113152]
     * position : Java工程师
     * roleType : 3
     * disabled : 0
     * photograph : dsafsdfsdf
     */

    private Long id;
    private String employeeName;
    private String telephone;
    private String employeeNumber;
    private String position;
    private int roleType;
    private int disabled;
    private String photograph;
    private List<Long> departmentIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
