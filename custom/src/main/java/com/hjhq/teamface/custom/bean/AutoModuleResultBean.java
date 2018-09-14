package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */

public class AutoModuleResultBean extends BaseBean {
    /**
     * data : {"dataList":[{"module_id":1222,"target_bean":"bean1535075811982","id":8,"title":"11","sorce_bean":"bean1536719360409"}]}
     */
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<DataListBean> dataList;

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            /**
             * module_id : 1222
             * target_bean : bean1535075811982
             * id : 8
             * title : 11
             * sorce_bean : bean1536719360409
             */

            private long module_id;
            private String target_bean;
            private long id;
            private String title;
            private String sorce_bean;

            public long getModule_id() {
                return module_id;
            }

            public void setModule_id(long module_id) {
                this.module_id = module_id;
            }

            public String getTarget_bean() {
                return target_bean;
            }

            public void setTarget_bean(String target_bean) {
                this.target_bean = target_bean;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSorce_bean() {
                return sorce_bean;
            }

            public void setSorce_bean(String sorce_bean) {
                this.sorce_bean = sorce_bean;
            }
        }
    }
}
