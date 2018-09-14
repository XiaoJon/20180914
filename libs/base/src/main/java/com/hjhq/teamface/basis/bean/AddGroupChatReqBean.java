package com.hjhq.teamface.basis.bean;

/**
 * @author Administrator
 * @anthor Administrator TEL:
 * @class describe
 */
public class AddGroupChatReqBean {

    /**
     * name : 群名称
     * notice : 深圳市南山区
     * peoples : 10，12
     * principal_name : 负责人
     * type : 1
     */

    private String name;
    private String notice;
    private String peoples;
    private String principal_name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getPrincipal_name() {
        return principal_name;
    }

    public void setPrincipal_name(String principal_name) {
        this.principal_name = principal_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}