package com.hjhq.teamface.oa.approve.bean;

/**
 * 审批通过请求Bean
 * Created by Administrator on 2018/1/12.
 */

public class ApprovePassRequestBean {


    /**
     * dataId : 2
     * processInstanceId : 40001
     * currentTaskId : 167575
     * taskDefinitionKey : task1
     * taskDefinitionName : 多人依次审批
     * nextAssignee : 1
     * message : 同意请假申请
     */

    private String dataId;
    private String moduleBean;
    private String processInstanceId;
    private String currentTaskId;
    private String taskDefinitionKey;
    private String taskDefinitionName;
    private String nextAssignee;
    private String message;
    private String imAprId;
    private String paramFields;
    //小助手必传
    private String astDataId;
    private Object data;

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

    public String getNextAssignee() {
        return nextAssignee;
    }

    public void setNextAssignee(String nextAssignee) {
        this.nextAssignee = nextAssignee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object detail) {
        this.data = detail;
    }

    public Object getData() {
        return data;
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

    public String getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(String moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String getAstDataId() {
        return astDataId;
    }

    public void setAstDataId(String astDataId) {
        this.astDataId = astDataId;
    }
}
