package com.hjhq.teamface.oa.approve.bean;

/**
 * 审批抄送请求Bean
 * Created by Administrator on 2018/1/12.
 */

public class ApproveCopyRequestBean {


    /**
     * processInstanceId : mofan:1:167524
     * taskDefinitionKey : task1
     * ccTo : 2,3,4
     */

    private String processInstanceId;
    private String taskDefinitionId;
    private String taskDefinitionKey;
    private String beanName;
    private String ccTo;
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

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getCcTo() {
        return ccTo;
    }

    public void setCcTo(String ccTo) {
        this.ccTo = ccTo;
    }

    public String getTaskDefinitionId() {
        return taskDefinitionId;
    }

    public void setTaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
