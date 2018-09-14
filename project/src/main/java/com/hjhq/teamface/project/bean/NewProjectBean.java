package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/4/23.
 * Describe：
 */

public class NewProjectBean {
    //项目名称
    private String name;
    //项目描述
    private String note;
    //项目可见范围
    private String visualRange;
    //负责人
    private String leader;
    //截止时间
    private String endTime;
    //模板id
    private String tempId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVisualRange() {
        return visualRange;
    }

    public void setVisualRange(String visualRange) {
        this.visualRange = visualRange;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}
