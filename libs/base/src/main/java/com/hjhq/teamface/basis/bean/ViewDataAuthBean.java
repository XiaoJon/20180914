package com.hjhq.teamface.basis.bean;

/**
 * 小助手信息
 */
public class ViewDataAuthBean extends BaseBean {

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
        /**
         * del_status : 0
         * moduleId : 2
         * readAuth : 0
         */
        //1已删除,0存在
        private String del_status;
        private String moduleId;
        //0有权限,1无权限
        private String readAuth;

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getReadAuth() {
            return readAuth;
        }

        public void setReadAuth(String readAuth) {
            this.readAuth = readAuth;
        }
    }
}
