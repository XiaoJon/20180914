package com.hjhq.teamface.basis.bean;

/**
 * 模块权限
 */
public class ModuleAuthBean extends BaseBean {
    /**
     * data : {"del_status":"0","moduleId":2,"readAuth":0}
     */
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        //0就没权限 1有权限
        private String readAuth;

        public String getReadAuth() {
            return readAuth;
        }

        public void setReadAuth(String readAuth) {
            this.readAuth = readAuth;
        }
    }
}
