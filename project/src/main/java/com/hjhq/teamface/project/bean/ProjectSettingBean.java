package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/4/23.
 * Describe：保存项目设置
 */

public class ProjectSettingBean {

    private String id;
    private String remindTitle;
    private String remindContent;
    private String remindUnit;
    private String remindType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemindTitle() {
        return remindTitle;
    }

    public void setRemindTitle(String remindTitle) {
        this.remindTitle = remindTitle;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public String getRemindUnit() {
        return remindUnit;
    }

    public void setRemindUnit(String remindUnit) {
        this.remindUnit = remindUnit;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }
}
