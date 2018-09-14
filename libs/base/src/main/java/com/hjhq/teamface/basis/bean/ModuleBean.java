package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/6/19
 */

public class ModuleBean extends BaseBean implements Serializable {

    /**
     * data : {"dataId":2,"moduleName":"订单jun","moduleId":"79","bean":"bean1529050483947"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * dataId : 2
         * moduleName : 订单jun
         * moduleId : 79
         * bean : bean1529050483947
         */

        private long dataId;
        private String moduleName;
        private String moduleId;
        private String bean;

        public long getDataId() {
            return dataId;
        }

        public void setDataId(long dataId) {
            this.dataId = dataId;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }
    }
}
