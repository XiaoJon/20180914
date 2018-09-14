package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 20:02 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/13 20:02
 * @change
 * @chang time
 * @class describe
 */
public class ActiveAlertRequestBean {
    public ActiveAlertRequestBean(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    private List<Long> employeeIds;

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
