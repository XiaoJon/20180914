package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class GetDepartmentStructureBean extends BaseBean {

    private List<MemberBean> data;

    public List<MemberBean> getData() {
        return data;
    }

    public void setData(List<MemberBean> data) {
        this.data = data;
    }

    public static class MemberBean extends Member {
        private int count;
        private int company_count;
        private List<MemberBean> childList;
        private List<Member> users;
        private String departmentId;
        private long parentId;
        private String sign;

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

        public int getCompany_count() {
            return company_count;
        }

        public void setCompany_count(int company_count) {
            this.company_count = company_count;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public long getParentId() {
            return parentId;
        }

        public void setParentId(long parentId) {
            this.parentId = parentId;
        }

    }
}
