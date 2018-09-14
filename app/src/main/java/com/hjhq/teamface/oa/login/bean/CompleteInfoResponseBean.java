package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class CompleteInfoResponseBean extends BaseBean {

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
        /**
         * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoiMSIsImF1ZCI6IjgiLCJpYXQiOjE1MDgxMjUzNzksImV4cCI6MTUwODEyNzE3OX0.6rgD-XNYe71jXnP8Wo_z88dImvLBvGtC2aKaEAxQ8mk
         */

        private String token;
        private long sign_id;
        private long company_id;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getSign_id() {
            return sign_id;
        }

        public void setSign_id(long sign_id) {
            this.sign_id = sign_id;
        }

        public long getCompany_id() {
            return company_id;
        }

        public void setCompany_id(long company_id) {
            this.company_id = company_id;
        }
    }
}