package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/7/26.
 * Describe：
 */

public class AddRemindBean {

    /**
     * id : 1
     * remindCockBeforeWork : 15
     * remindClockAfterWork : 30
     */

    private String id;
    private int remindCockBeforeWork;
    private int remindClockAfterWork;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRemindCockBeforeWork() {
        return remindCockBeforeWork;
    }

    public void setRemindCockBeforeWork(int remindCockBeforeWork) {
        this.remindCockBeforeWork = remindCockBeforeWork;
    }

    public int getRemindClockAfterWork() {
        return remindClockAfterWork;
    }

    public void setRemindClockAfterWork(int remindClockAfterWork) {
        this.remindClockAfterWork = remindClockAfterWork;
    }
}
