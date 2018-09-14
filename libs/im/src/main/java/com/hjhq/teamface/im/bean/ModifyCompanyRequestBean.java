package com.hjhq.teamface.im.bean;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 21:00 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/13 21:00
 * @change
 * @chang time
 * @class describe
 */
public class ModifyCompanyRequestBean {
    public ModifyCompanyRequestBean(long id, String companyName, String telephone, String companyAddress, String industryCode) {
        this.id = id;
        this.companyName = companyName;
        this.telephone = telephone;
        this.companyAddress = companyAddress;
        this.industryCode = industryCode;
    }

    /**
     * id : 3329542428049408
     * companyName : 颜值印象2222
     * telephone : 0755-123456
     * companyAddress : 深圳市南山区
     * industryCode : 5145,5454
     */

    private long id;
    private String companyName;
    private String telephone;
    private String companyAddress;
    private String industryCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
}
