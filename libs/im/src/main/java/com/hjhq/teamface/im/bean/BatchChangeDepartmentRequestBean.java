package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 18:26 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class 批调整部门
 * @anthor Administrator TEL:
 * @time 2017/5/13 18:26
 * @change
 * @chang time
 * @class describe
 */
public class BatchChangeDepartmentRequestBean {

    public BatchChangeDepartmentRequestBean(long id, List<Long> employeeIds) {
        this.id = id;
        this.employeeIds = employeeIds;
    }

    /**
     * employeeIds : [3356812542001152]
     * id : 3341102437433344
     */

    private long id;
    private List<Long> employeeIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
