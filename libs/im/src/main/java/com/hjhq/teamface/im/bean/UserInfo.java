package com.hjhq.teamface.im.bean;

public class UserInfo {
    /**
     * departmentName : 深圳汇聚华企科技有限公司
     * employeeName : 肖俊
     * photograph : null
     * imUserName : 2f07ed6362f8a61ad352906f6c8be113
     * gender : 0
     * companyName : 深圳汇聚华企科技有限公司
     * microblogBackground : null
     * telephone : 13528885786
     * position : null
     * region : 110000,undefined,undefined
     * email :
     */

    private String departmentName;
    private String employeeName;
    private String photograph;
    private String imUserName;
    private int gender;
    private String companyName;
    private String microblogBackground;
    private String telephone;
    private String position;
    private String region;
    private String email;

    public UserInfo(String departmentName, String employeeName, String photograph,
            String imUserName, int gender, String companyName,
            String microblogBackground, String telephone, String position,
            String region, String email) {
        this.departmentName = departmentName;
        this.employeeName = employeeName;
        this.photograph = photograph;
        this.imUserName = imUserName;
        this.gender = gender;
        this.companyName = companyName;
        this.microblogBackground = microblogBackground;
        this.telephone = telephone;
        this.position = position;
        this.region = region;
        this.email = email;
    }

    public UserInfo() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public String getImUserName() {
        return imUserName;
    }

    public void setImUserName(String imUserName) {
        this.imUserName = imUserName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMicroblogBackground() {
        return microblogBackground;
    }

    public void setMicroblogBackground(String microblogBackground) {
        this.microblogBackground = microblogBackground;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
