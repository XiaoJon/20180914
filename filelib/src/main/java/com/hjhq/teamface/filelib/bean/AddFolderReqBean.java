package com.hjhq.teamface.filelib.bean;

/**
 * @author Administrator
 * @date 2017/11/13
 * Describe：公司列表
 */

public class AddFolderReqBean {


    /**
     * name : 子级测试
     * color : #CCCCCC
     * type : 0
     * style : 1
     * manage_by : 3
     * member_by : 3
     * parent_id :
     */

    private String name;
    private String color;
    private String type;
    private int style;
    private String manage_by = "";
    private String member_by = "";
    private String parent_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getManage_by() {
        return manage_by;
    }

    public void setManage_by(String manage_by) {
        this.manage_by = manage_by;
    }

    public String getMember_by() {
        return member_by;
    }

    public void setMember_by(String member_by) {
        this.member_by = member_by;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
