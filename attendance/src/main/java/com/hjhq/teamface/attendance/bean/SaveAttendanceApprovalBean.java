package com.hjhq.teamface.attendance.bean;

/**
 * 保存或修改自定义数据
 * Created by lx on 2017/9/14.
 */

public class SaveAttendanceApprovalBean {

    /**
     * relevanceType  : 0
     * relevanceModuleName : 请假
     * relevanceModuleBean : approve
     * relevanceModuleId : approve
     * relevanceDataId  : 5245234523452345345
     * relevanceStatus  : 0
     * startTime  : 111
     * endTime  : 22
     * duration  : 3
     * durationUnit  : 0
     */

    private int relevanceType;
    private String relevanceModuleName;
    private String relevanceModuleBean;
    private String relevanceModuleId;
    private long relevanceDataId;
    private int relevanceStatus;
    private Long startTime;
    private Long endTime;
    private Double duration;
    private int durationUnit;

    public int getRelevanceType() {
        return relevanceType;
    }

    public void setRelevanceType(int relevanceType) {
        this.relevanceType = relevanceType;
    }

    public String getRelevanceModuleName() {
        return relevanceModuleName;
    }

    public void setRelevanceModuleName(String relevanceModuleName) {
        this.relevanceModuleName = relevanceModuleName;
    }

    public String getRelevanceModuleBean() {
        return relevanceModuleBean;
    }

    public void setRelevanceModuleBean(String relevanceModuleBean) {
        this.relevanceModuleBean = relevanceModuleBean;
    }

    public String getRelevanceModuleId() {
        return relevanceModuleId;
    }

    public void setRelevanceModuleId(String relevanceModuleId) {
        this.relevanceModuleId = relevanceModuleId;
    }

    public long getRelevanceDataId() {
        return relevanceDataId;
    }

    public void setRelevanceDataId(long relevanceDataId) {
        this.relevanceDataId = relevanceDataId;
    }

    public int getRelevanceStatus() {
        return relevanceStatus;
    }

    public void setRelevanceStatus(int relevanceStatus) {
        this.relevanceStatus = relevanceStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public int getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(int durationUnit) {
        this.durationUnit = durationUnit;
    }
}
