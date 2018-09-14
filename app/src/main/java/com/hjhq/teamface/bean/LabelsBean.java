package com.hjhq.teamface.bean;

/**
 * 流式样式bean
 * @author Administrator
 * @date 2017/11/8
 */

public class LabelsBean {
    /**
     * id : 3482758568837120
     * createDate : 3482758568837122
     * disabled : null
     * taskId : 3479946421944320
     * labelId : 3476679999684608
     * labelName : 帮你健康2
     * labelColor : #34a7b6
     */

    private long id;
    private long createDate;
    private Object disabled;
    private long taskId;
    private long labelId;
    private String labelName;
    private String labelColor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public Object getDisabled() {
        return disabled;
    }

    public void setDisabled(Object disabled) {
        this.disabled = disabled;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }
}
