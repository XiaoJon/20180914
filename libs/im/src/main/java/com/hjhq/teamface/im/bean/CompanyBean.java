package com.hjhq.teamface.im.bean;


/**
 * 公司
 * Created by lx on 2017/5/25.
 */
public class CompanyBean {
    /**
     * photograph : null
     * isDefault : 1
     * companyName : 程林根的团队
     * id : 3346843534082048
     */

    private String photograph;
    private int isDefault;
    private String companyName;
    private long id;

    public CompanyBean(String photograph, int isDefault, String companyName,
            long id) {
        this.photograph = photograph;
        this.isDefault = isDefault;
        this.companyName = companyName;
        this.id = id;
    }

    public CompanyBean() {
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
