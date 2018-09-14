package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class ViewDataAuthResBean extends BaseBean implements Serializable {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        //0无权限 1有权限 2数据已删除
        private String readAuth;

        public String getReadAuth() {
            return readAuth;
        }

        public void setReadAuth(String readAuth) {
            this.readAuth = readAuth;
        }
    }
}
