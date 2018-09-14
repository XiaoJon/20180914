package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/7/11.
 */

public class QuoteRelationRequestBean {
    //项目id
    private long projectId;
    private long bean_type;
    //任务ID (或者子任务ID)
    private long taskId;
    //模块的bean
    private String bean;
    //1 属于任务的关联 ，2属于子任务的关联
    private long taskType;
    //引用的记录ID（多条数据id以，号分割）
    private String quoteTaskId;
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

    public long getBean_type() {
        return bean_type;
    }

    public void setBean_type(long bean_type) {
        this.bean_type = bean_type;
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

    public long getTaskType() {
        return taskType;
    }

    public void setTaskType(long taskType) {
        this.taskType = taskType;
    }

    public String getQuoteTaskId() {
        return quoteTaskId;
    }

    public void setQuoteTaskId(String quoteTaskId) {
        this.quoteTaskId = quoteTaskId;
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
}
