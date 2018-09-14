package com.hjhq.teamface.oa.approve.bean;

/**
 * 审批撤销请求Bean
 * Created by Administrator on 2018/1/12.
 */

public class ApproveRevokeRequestBean {

    /**
     * processInstanceId : mofan:1:167524
     * taskDefinitionKey : task2
     * taskDefinitionName : BOSS审批
     */

    private String processInstanceId;
    private String currentTaskId;
    private String taskDefinitionKey;
    private String taskDefinitionName;
    private String moduleBean;
    private String dataId;



    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
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
}
