package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * 调整部门请求bean
 */
public class ModDepartmentRequestBean {

    public ModDepartmentRequestBean(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public ModDepartmentRequestBean(Long id, List<Long> employeeIds) {
        this.id = id;
        this.employeeIds = employeeIds;
    }

    /**
     * employeeIds : [3341081743540224]
     * id : 0
     * 0启用1停用
     */

    private Long id;
    private List<Long> employeeIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
