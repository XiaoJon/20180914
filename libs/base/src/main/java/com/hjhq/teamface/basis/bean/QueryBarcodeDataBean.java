package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

public class QueryBarcodeDataBean extends BaseBean implements Serializable {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String bean;
        //0无权限,1有权限,2数据不存在
        private String readAuth;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }

        public String getReadAuth() {
            return readAuth;
        }

        public void setReadAuth(String readAuth) {
            this.readAuth = readAuth;
        }
    }

}