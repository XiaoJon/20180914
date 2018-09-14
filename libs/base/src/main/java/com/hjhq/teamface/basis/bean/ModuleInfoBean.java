package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

public class ModuleInfoBean implements Serializable {
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