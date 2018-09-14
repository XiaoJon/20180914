package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.database.Member;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：组织架构导航数据实体类
 */

public class OrganizationNaviData implements Serializable {
    String organizationName;
    String organizationId;
    int organizationLevel;
    private List<GetDepartmentStructureBean.MemberBean> dataBeen;
    private List<Member> memberList;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getOrganizationLevel() {
        return organizationLevel;
    }

    public void setOrganizationLevel(int organizationLevel) {
        this.organizationLevel = organizationLevel;
    }

    public List<GetDepartmentStructureBean.MemberBean> getDataBeen() {
        return dataBeen;
    }

    public void setDataBeen(List<GetDepartmentStructureBean.MemberBean> dataBeen) {
        this.dataBeen = dataBeen;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
