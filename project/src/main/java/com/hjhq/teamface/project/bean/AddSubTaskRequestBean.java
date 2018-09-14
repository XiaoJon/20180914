package com.hjhq.teamface.project.bean;

/**
 * 新增子任务
 *
 * @author Administrator
 * @date 2018/6/19
 */

public class AddSubTaskRequestBean {

    /**
     * projectId : 21
     * dataId : 1159
     * subnodeId : 166
     * bean : memo
     */

    private long projectId;
    private long taskId;
    private String bean;
    private String endTime;
    private String executorId;
    private String name;
    private String checkStatus;
    private String checkMember;
    private String associatesStatus;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
