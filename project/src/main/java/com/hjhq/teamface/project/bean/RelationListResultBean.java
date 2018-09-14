package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.common.bean.TaskInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class RelationListResultBean extends BaseBean {

    /**
     * data : {"dataList":[]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ModuleDataBean> dataList;

        public List<ModuleDataBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<ModuleDataBean> dataList) {
            this.dataList = dataList;
        }

        public static class ModuleDataBean {
            private String module_name;
            private List<TaskInfoBean> moduleDataList;

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public List<TaskInfoBean> getModuleDataList() {
                return moduleDataList;
            }

            public void setModuleDataList(List<TaskInfoBean> moduleDataList) {
                this.moduleDataList = moduleDataList;
            }
        }
    }
}
