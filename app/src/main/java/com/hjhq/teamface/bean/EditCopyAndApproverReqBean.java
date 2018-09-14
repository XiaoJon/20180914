package com.hjhq.teamface.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/6/21 19:49 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.bean
 * @class describe
 * @anthor Administrator TEL:13163739593
 * @time 2017/6/21 19:49
 * @change
 * @chang time
 * @class describe
 */
public class EditCopyAndApproverReqBean {

    public EditCopyAndApproverReqBean(String moduleSonId, Long employeeId, int isType, List<Long> employeeIdList) {
        this.moduleSonId = moduleSonId;
        this.employeeId = employeeId;
        this.isType = isType;
        this.employeeIdList = employeeIdList;
    }

    public EditCopyAndApproverReqBean(String moduleSonId, int isType, List<Long> employeeIdList) {
        this.moduleSonId = moduleSonId;
        this.isType = isType;
        this.employeeIdList = employeeIdList;
    }

    /**
     * {
     "moduleSonId":"33385754688831393",//模块的ID，就是计划，日报周报的模块ID
     "employeeId":"3341292543180800",//当前登录的员工ID
     "isType":"1",// 0是审批人，1表示抄送人
     "employeeIdList": [//审批人或者是抄送人的ID集合
     "3341121262305280",
     "3341121723891712"
     ]
     }
     */

    private String moduleSonId;
    private Long employeeId;
    private int isType;
    private List<Long> employeeIdList;

    public String getModuleSonId() {
        return moduleSonId;
    }

    public void setModuleSonId(String moduleSonId) {
        this.moduleSonId = moduleSonId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public int getIsType() {
        return isType;
    }

    public void setIsType(int isType) {
        this.isType = isType;
    }

    public List<Long> getEmployeeIdList() {
        return employeeIdList;
    }

    public void setEmployeeIdList(List<Long> employeeIdList) {
        this.employeeIdList = employeeIdList;
    }
}
