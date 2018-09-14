package com.hjhq.teamface.custom.bean;

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
    private List<MemberBean> dataBeen;
    private List<Member> memberList;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public static class MemberBean extends Member {
        private int count;
        private List<MemberBean> childList;
        private List<Member> users;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<MemberBean> getChildList() {
            return childList;
        }

        public void setChildList(List<MemberBean> childList) {
            this.childList = childList;
        }

        public List<Member> getUsers() {
            return users;
        }

        public void setUsers(List<Member> users) {
            this.users = users;
        }
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

    public List<MemberBean> getDataBeen() {
        return dataBeen;
    }

    public void setDataBeen(List<MemberBean> dataBeen) {
        this.dataBeen = dataBeen;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
