package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/6/27.
 * Describe：
 */

public class MemberOrDepartmentBean {

    /**
     * name : 名字
     * id : 4
     * picture :
     * type : 1
     * value : 1-4
     * sign_id :
     */

    private String name;
    private String id;
    private String picture;
    private String type;
    private String value;
    private String sign_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSign_id() {
        return sign_id;
    }

    public void setSign_id(String sign_id) {
        this.sign_id = sign_id;
    }
}
