package com.hjhq.teamface.im.bean;



/**
 * 角色实体类
 * Created by lx on 2017/5/31.
 */
public class RoleBean {
    /**
     * id : 3350770226724864
     * createDate : 1493349759003
     * disabled : 0
     * roleName : 所有者
     * roleType : 1
     * companyId : 3350770212126720
     */
    private long id;
    private long createDate;
    private int disabled;
    private String roleName;
    private int roleType;
    private long companyId;

    public RoleBean(long id, long createDate, int disabled, String roleName,
            int roleType, long companyId) {
        this.id = id;
        this.createDate = createDate;
        this.disabled = disabled;
        this.roleName = roleName;
        this.roleType = roleType;
        this.companyId = companyId;
    }

    public RoleBean() {
    }

    public  RoleBean(int roleType, String roleName){
        this.roleName = roleName;
        this.roleType = roleType;
    }

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

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
