package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 20:50 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/13 20:50
 * @change
 * @chang time
 * @class describe
 */
public class VerifyEmployeeRequestBean {
    public VerifyEmployeeRequestBean(int type, List<Long> employeeIds) {
        this.type = type;
        this.employeeIds = employeeIds;
    }

    /**
     * employeeIds : [3350981622972416]
     * type : 1
     * 0移除1通过
     */

    private int type;
    private List<Long> employeeIds;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
