package com.hjhq.teamface.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/10/9
 * Describe：登录后数据
 */

public class LoginResponseBean extends BaseBean {

    /**
     * data : {"companyId":15555555555555551,"perfect":"2","auths":[],"employeeInfo":{},"userCompanyInfo":{},"userId":555555555555559,"token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNiIsInN1YiI6IjExIiwiaWF0IjoxNTA3NjI3NTYyLCJleHAiOjE1MDc2MjgxNjJ9.HteTDVu6D78RHhoGX_w5E2_GhVX8mhYZANn4zY8Gs5I"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * companyId : 15555555555555551
         * perfect : 2
         * auths : []
         * employeeInfo : {}
         * userCompanyInfo : {}
         * userId : 555555555555559
         * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNiIsInN1YiI6IjExIiwiaWF0IjoxNTA3NjI3NTYyLCJleHAiOjE1MDc2MjgxNjJ9.HteTDVu6D78RHhoGX_w5E2_GhVX8mhYZANn4zY8Gs5I
         */

        private long company_id;
        private long sign_id;
        private String perfect;
        //修改密码安全策略后 返回0 需要去修改密码
        private String term_sign;
        private String isCompany;
        private EmployeeInfoBean employeeInfo;
        private UserCompanyInfoBean userCompanyInfo;
        private long userId;
        private String token;
        private List<?> auths;


        public long getSign_id() {
            return sign_id;
        }

        public void setSign_id(long sign_id) {
            this.sign_id = sign_id;
        }

        public String getPerfect() {
            return perfect;
        }

        public void setPerfect(String perfect) {
            this.perfect = perfect;
        }

        public String getIsCompany() {
            return isCompany;
        }

        public void setIsCompany(String isCompany) {
            this.isCompany = isCompany;
        }

        public EmployeeInfoBean getEmployeeInfo() {
            return employeeInfo;
        }

        public void setEmployeeInfo(EmployeeInfoBean employeeInfo) {
            this.employeeInfo = employeeInfo;
        }

        public UserCompanyInfoBean getUserCompanyInfo() {
            return userCompanyInfo;
        }

        public void setUserCompanyInfo(UserCompanyInfoBean userCompanyInfo) {
            this.userCompanyInfo = userCompanyInfo;
        }

        public long getCompany_id() {
            return company_id;
        }

        public void setCompany_id(long company_id) {
            this.company_id = company_id;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getTerm_sign() {
            return term_sign;
        }

        public void setTerm_sign(String term_sign) {
            this.term_sign = term_sign;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<?> getAuths() {
            return auths;
        }

        public void setAuths(List<?> auths) {
            this.auths = auths;
        }

        public static class EmployeeInfoBean {
        }

        public static class UserCompanyInfoBean {
        }
    }
}
