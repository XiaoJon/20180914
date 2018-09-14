package com.hjhq.teamface.oa.login.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class CompanyStructureBean extends BaseBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 0
         * checked : false
         * childList : [{"count":0,"checked":false,"childList":[{"count":0,"checked":false,"childList":[],"id":"2","text":"人事","parentId":"3"},{"count":0,"checked":false,"childList":[{"count":0,"checked":false,"childList":[],"id":"9","text":"行政部","parentId":"1"},{"count":0,"checked":false,"childList":[],"id":"4","text":"java开发","parentId":"1"}],"id":"1","text":"研发部门","parentId":"3"}],"id":"3","text":"总经办","parentId":"5"}]
         * id : 5
         * text : 汇聚华企
         */

        private String count;
        private String company_count;
        private boolean checked;
        private String id;
        private String sign;
        private String value;
        private String name;
        private List<DataBean> childList;
        private List<UserBean> users;

        private int selectState;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCompany_count() {
            return company_count;
        }

        public void setCompany_count(String company_count) {
            this.company_count = company_count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSelectState() {
            return selectState;
        }

        public void setSelectState(int selectState) {
            this.selectState = selectState;
        }

        public List<DataBean> getChildList() {
            return childList;
        }


        public void setChildList(List<DataBean> childList) {
            this.childList = childList;
        }

        public List<UserBean> getUsers() {
            return users;
        }

        public void setUsers(List<UserBean> users) {
            this.users = users;
        }
    }

    public static class UserBean {

        /**
         * departmentId : 14
         * post_name : 000
         * count : 0
         * checked : false
         * employee_name : 8888
         * id : 21
         */

        private String departmentId;
        private String post_name;
        private String count;
        private boolean checked;
        private String employee_name;
        private String name;
        private String picture;
        private String id;
        private String sign_id;
        private String value;

        private int selectState;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getPost_name() {
            return post_name;
        }

        public void setPost_name(String post_name) {
            this.post_name = post_name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSign_id() {
            return sign_id;
        }

        public void setSign_id(String sign_id) {
            this.sign_id = sign_id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getSelectState() {
            return selectState;
        }

        public void setSelectState(int selectState) {
            this.selectState = selectState;
        }
    }

}
