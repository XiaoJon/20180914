package com.hjhq.teamface.basis.database;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author lx
 * @date 2017/3/29
 */
@Entity
public class Member implements Serializable {
    @Transient
    private static final long serialVersionUID = -1472304832335192835L;
    long updateTime;


    protected String myId;
    protected String is_enable;
    protected String post_name;
    protected String post_id;
    protected String employee_name;
    protected String role_name;
    protected String department_name;
    protected String phone;
    protected String mobile_phone;
    protected String sign_id;
    protected String picture;
    protected String email;
    protected String company_id;
    //0部门 1成员 2角色 3动态参数  4全公司   默认成员
    protected int type = 1;
    //id（员工、部门、角色通用）
    @Id
    protected long id;
    //名称（员工、部门、角色通用）
    protected String name;
    //type:id   给后台判断用的

    protected String value;

    //非人员、部门、角色字段

    @Transient
    protected boolean check;


    //是否参与选择,用于不可更改该或不能参与的成员条目
    @Transient
    protected int selectState = 1;

    public Member() {
    }


    public Member(long id, String text, int type) {
        setId(id);
        setName(text);
        setType(type);
    }

    public Member(long id, String text, int type, String picture) {
        setId(id);
        setName(text);
        setType(type);
        setPicture(picture);
    }


    @Generated(hash = 758763409)
    public Member(long updateTime, String myId, String is_enable, String post_name,
            String post_id, String employee_name, String role_name,
            String department_name, String phone, String mobile_phone,
            String sign_id, String picture, String email, String company_id,
            int type, long id, String name, String value) {
        this.updateTime = updateTime;
        this.myId = myId;
        this.is_enable = is_enable;
        this.post_name = post_name;
        this.post_id = post_id;
        this.employee_name = employee_name;
        this.role_name = role_name;
        this.department_name = department_name;
        this.phone = phone;
        this.mobile_phone = mobile_phone;
        this.sign_id = sign_id;
        this.picture = picture;
        this.email = email;
        this.company_id = company_id;
        this.type = type;
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign_id() {
        return sign_id;
    }

    public void setSign_id(String sign_id) {
        this.sign_id = sign_id;
    }

    public void setSign_id(long sign_id) {
        this.sign_id = sign_id + "";
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return name;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)) {
            switch (type) {
                case 0:
                    return department_name;
                case 1:
                    return getEmployee_name();
                case 2:
                    return role_name;
                case 3:
                    return " ";
                default:
                    break;
            }

        }
        return name;
    }

    public void setName(String text) {
        this.name = text;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
        if (TextUtils.isEmpty(employee_name)) {
            this.name = employee_name;
        }
    }

    public int getSelectState() {
        return selectState;
    }

    public void setSelectState(int selectState) {
        this.selectState = selectState;
    }


    public String getCompany_id() {
        return this.company_id;
    }


    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
