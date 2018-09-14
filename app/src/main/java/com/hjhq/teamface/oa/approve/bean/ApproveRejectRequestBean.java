package com.hjhq.teamface.oa.approve.bean;

/**
 * 审批驳回请求Bean
 * Created by Administrator on 2018/1/12.
 */

public class ApproveRejectRequestBean {


    /**
     * bean : bean1514191464504
     * processInstanceId : mofan:1:167524
     * currentTaskId : 167579
     * taskDefinitionKey : task2
     * taskDefinitionName : BOSS审批
     * rejectType : 0
     * message : 333驳回上一节点
     */

    private String moduleBean ;
    private String dataId ;
    private String processInstanceId;
    private String currentTaskId;
    private String taskDefinitionKey;
    private String taskDefinitionName;
    private String rejectType;
    private String rejectToTaskKey;
    private String message;
    private String imAprId;
    private String paramFields;
    private Object data;
    private String astDataId;


    public String getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(String moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTaskDefinitionName() {
        return taskDefinitionName;
    }

    public void setTaskDefinitionName(String taskDefinitionName) {
        this.taskDefinitionName = taskDefinitionName;
    }

    public String getRejectType() {
        return rejectType;
    }

    public void setRejectType(String rejectType) {
        this.rejectType = rejectType;
    }

    public String getRejectToTaskKey() {
        return rejectToTaskKey;
    }

    public void setRejectToTaskKey(String rejectToTaskKey) {
        this.rejectToTaskKey = rejectToTaskKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImAprId() {
        return imAprId;
    }

    public void setImAprId(String imAprId) {
        this.imAprId = imAprId;
    }

    public String getParamFields() {
        return paramFields;
    }

    public void setParamFields(String paramFields) {
        this.paramFields = paramFields;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setAstDataId(String astDataId) {
        this.astDataId = astDataId;
    }
}
