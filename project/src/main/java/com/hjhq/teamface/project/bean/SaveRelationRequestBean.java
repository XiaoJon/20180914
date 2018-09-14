package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/7/11.
 */

public class SaveRelationRequestBean {
    //项目id
    private long projectId;
    //子节点ID
    private long subnodeId;
    //数据ID
    private long dataId;
    //任务ID (或者子任务ID)
    private long taskId;
    //检验状态 0否 1是
    private String checkStatus;
    //检验人
    private String checkMember;
    //仅参与者可见 0否 1是
    private String associatesStatus;
    //模块的bean
    private String bean;
    //1 属于任务的关联 ，2属于子任务的关联
    private long taskType;
    //任务名称
    private String taskName;
    //截止时间（时间戳）
    private String endTime;
    //执行人
    private String executorId;
    //子任务截止时间（时间戳）
    private String startTime;
    //被引用bean类型 1备忘录 2任务 3自定义 4 审批
    private String bean_type;

    //模块的id
    private String moduleId;
    //模块的模块名称
    private String moduleName;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getSubnodeId() {
        return subnodeId;
    }

    public void setSubnodeId(long subnodeId) {
        this.subnodeId = subnodeId;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
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

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public long getTaskType() {
        return taskType;
    }

    public void setTaskType(long taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBean_type() {
        return bean_type;
    }

    public void setBean_type(String bean_type) {
        this.bean_type = bean_type;
    }
}
