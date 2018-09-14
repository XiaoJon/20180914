package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * @author Administrator
 * @date 2017/11/24
 */

public class SwitchCompanyResultBean extends BaseBean {
    /**
     * data : {"token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoiMSIsImF1ZCI6IjgiLCJpYXQiOjE1MDgxMjUzNzksImV4cCI6MTUwODEyNzE3OX0.6rgD-XNYe71jXnP8Wo_z88dImvLBvGtC2aKaEAxQ8mk"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String perfect;
        private String isCompany;
        private String token;

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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
