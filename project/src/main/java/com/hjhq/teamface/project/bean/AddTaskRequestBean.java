package com.hjhq.teamface.project.bean;

/**
 * 新增任务
 *
 * @author Administrator
 * @date 2018/6/19
 */

public class AddTaskRequestBean {

    /**
     * projectId : 21
     * dataId : 1159
     * subnodeId : 166
     * bean : memo
     */

    private long projectId;
    private long dataId;
    private long subnodeId;
    private String checkStatus;
    private String checkMember;
    private String associatesStatus;
    private String bean;
    private String moduleName;
    private String taskName;
    private String remark;
    private String moduleId;
    private String endTime;
    private String executorId;
    private String startTime;
    private String id;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getSubnodeId() {
        return subnodeId;
    }

    public void setSubnodeId(long subnodeId) {
        this.subnodeId = subnodeId;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckMember() {
        return checkMember;
    }

    public void setCheckMember(String checkMember) {
        this.checkMember = checkMember;
    }

    public String getAssociatesStatus() {
        return associatesStatus;
    }

    public void setAssociatesStatus(String associatesStatus) {
        this.associatesStatus = associatesStatus;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
