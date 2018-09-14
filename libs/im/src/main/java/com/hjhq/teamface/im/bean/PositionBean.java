package com.hjhq.teamface.im.bean;


import java.io.Serializable;

/**
 * 职位
 * Created by lx on 2017/5/26.
 */
public class PositionBean implements Serializable{
    private static final long serialVersionUID = 8281815220625236205L;
    /**
     * id : 3365301732261888
     * createDate : 1494236691714
     * disabled : null
     * companyId : 3350770212126720
     * position : iOS
     * isDefault : 0
     */
    private long id;
    private long createDate;
    private int disabled;
    private long companyId;
    private String position;
    private int isDefault;




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

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}