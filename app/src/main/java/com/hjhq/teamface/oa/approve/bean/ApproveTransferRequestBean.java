package com.hjhq.teamface.oa.approve.bean;

/**
 * 审批转移请求Bean
 * Created by Administrator on 2018/1/12.
 */

public class ApproveTransferRequestBean {

    /**
     * processInstanceId : caojianhua:1:85013
     * currentTaskId : 87503
     * taskDefinitionKey : task1
     * taskDefinitionName : 部门经理审批
     * approver : 1
     * message : 转交给1用户（曹建华）
     */

    private String processInstanceId;
    private String currentTaskId;
    private String taskDefinitionKey;
    private String taskDefinitionName;
    private String approver;
    private String message;
    private String moduleBean;
    private String dataId;
    private Object data;
    private String imAprId;
    private String paramFields;
    private String astDataId;


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

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMessage() {
        return message;
    }

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

    public void setAstDataId(String astDataId) {
        this.astDataId = astDataId;
    }
}
