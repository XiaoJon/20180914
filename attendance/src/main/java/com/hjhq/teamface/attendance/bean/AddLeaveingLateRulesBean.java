package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/7/26.
 * Describe：
 */

public class AddLeaveingLateRulesBean {

    /**
     * id : 1
     * remindCockBeforeWork : 15
     * remindClockAfterWork : 30
     */

    //规则id
    private String id;
    //晚到时间
    private int workLateTime;
    //晚走时间
    private int leaveingLateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWorkLateTime() {
        return workLateTime;
    }

    public void setWorkLateTime(int workLateTime) {
        this.workLateTime = workLateTime;
    }

    public int getLeaveingLateTime() {
        return leaveingLateTime;
    }

    public void setLeaveingLateTime(int leaveingLateTime) {
        this.leaveingLateTime = leaveingLateTime;
    }
}
