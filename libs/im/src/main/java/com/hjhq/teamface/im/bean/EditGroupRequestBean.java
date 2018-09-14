package com.hjhq.teamface.im.bean;

/**
 * Created by lx on 2017/6/1.
 */

public class EditGroupRequestBean {

    private String groupId;//群ID
    private String desc;//群描述
    private String name;//群名称
    private String background;//群背景

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
