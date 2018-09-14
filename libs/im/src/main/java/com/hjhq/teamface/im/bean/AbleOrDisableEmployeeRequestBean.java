package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 17:59 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class
 * @anthor Administrator TEL:
 * @time 2017/5/13 17:59
 * @change
 * @chang time
 * @class describe
 */
public class AbleOrDisableEmployeeRequestBean {

    public AbleOrDisableEmployeeRequestBean(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public AbleOrDisableEmployeeRequestBean(int disabled, List<Long> employeeIds) {
        this.disabled = disabled;
        this.employeeIds = employeeIds;
    }

    /**
     * employeeIds : [3341081743540224]
     * disabled : 0
     * 0启用1停用
     */

    private int disabled;
    private List<Long> employeeIds;

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
