package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/9/6.
 * Describe：
 */

public class LocalModuleBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 审批
         * id : 1
         * onoff_status : 1
         * bean : approval
         */

        private String name;
        private String id;
        private String onoff_status;
        private String bean;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOnoff_status() {
            return onoff_status;
        }

        public void setOnoff_status(String onoff_status) {
            this.onoff_status = onoff_status;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }
    }
}
