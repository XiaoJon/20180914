package com.hjhq.teamface.im.bean;

/**
 * Created by lx on 2017/5/25.
 */
public class MyEmployeeBean {


    /**
     * companyId : 3350770212126720
     * createDate : 0
     * disabled : 0
     * employeeAge : 0
     * employeeName : 尹明亮
     * employeeStatus : 0
     * gender : 0
     * id : 3350770213208064
     * imPassword : 7a33e98edeacb7955684bea1ff5f96a7
     * imRegistrationId : e6e152baeb25757112a41f0d312b86ac
     * imUsername":"e6e152baeb25757112a41f0d312b86ac"
     * incumbency : 0
     * isPrincipal : 0
     * maritalStatus : 0
     * positionId : 0
     * roleType : 0
     * telephone : 15818548636
     * userId : 3350769185210368
     * verifyStatus : 1
     */

    private long companyId;
    private long createDate;
    private int disabled;
    private int employeeAge;
    private String employeeName;
    private int employeeStatus;
    private int gender;
    private long id;
    private String imPassword;
    private String imRegistrationId;
    private long incumbency;
    private int isPrincipal;
    private int maritalStatus;
    private long positionId;
    private int roleType;
    private String telephone;
    private long userId;
    private int verifyStatus;

    public MyEmployeeBean(long companyId, long createDate, int disabled,
                          int employeeAge, String employeeName, int employeeStatus, int gender,
                          long id, String imPassword, String imRegistrationId, long incumbency,
                          int isPrincipal, int maritalStatus, long positionId, int roleType,
                          String telephone, long userId, int verifyStatus) {
        this.companyId = companyId;
        this.createDate = createDate;
        this.disabled = disabled;
        this.employeeAge = employeeAge;
        this.employeeName = employeeName;
        this.employeeStatus = employeeStatus;
        this.gender = gender;
        this.id = id;
        this.imPassword = imPassword;
        this.imRegistrationId = imRegistrationId;
        this.incumbency = incumbency;
        this.isPrincipal = isPrincipal;
        this.maritalStatus = maritalStatus;
        this.positionId = positionId;
        this.roleType = roleType;
        this.telephone = telephone;
        this.userId = userId;
        this.verifyStatus = verifyStatus;
    }

    public MyEmployeeBean() {
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(int employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImPassword() {
        return imPassword;
    }

    public void setImPassword(String imPassword) {
        this.imPassword = imPassword;
    }

    public String getImRegistrationId() {
        return imRegistrationId;
    }

    public void setImRegistrationId(String imRegistrationId) {
        this.imRegistrationId = imRegistrationId;
    }

    public long getIncumbency() {
        return incumbency;
    }

    public void setIncumbency(int incumbency) {
        this.incumbency = incumbency;
    }

    public int getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(int isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public void setIncumbency(long incumbency) {
        this.incumbency = incumbency;
    }
}
