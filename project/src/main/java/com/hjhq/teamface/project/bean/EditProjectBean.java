package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/4/23.
 * Describe：
 */

public class EditProjectBean {

    private long id;
    private String name;
    private long leader;
    private int progressStatus;
    private int progressContent;
    private long startTime;
    private long endTime;
    private int visualRange;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLeader() {
        return leader;
    }

    public void setLeader(long leader) {
        this.leader = leader;
    }

    public int getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(int progressStatus) {
        this.progressStatus = progressStatus;
    }

    public int getProgressContent() {
        return progressContent;
    }

    public void setProgressContent(int progressContent) {
        this.progressContent = progressContent;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getVisualRange() {
        return visualRange;
    }

    public void setVisualRange(int visualRange) {
        this.visualRange = visualRange;
    }

    public String getDescribe() {
        return note;
    }

    public void setDescribe(String describe) {
        this.note = describe;
    }
}
