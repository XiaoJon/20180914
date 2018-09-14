package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

public class QuoteTaskResultBean extends BaseBean {

    /**
     * data : {"dataList":[{"task_name":"","project_custom_id":340,"bean_name":"bean1529636406668","module_id":0,"end_time":1535702871449,"employee_name":"","id":245,"module_name":"","relation_id":3},{"task_name":"","project_custom_id":339,"bean_name":"project_custom","module_id":0,"end_time":1535702759087,"employee_name":"","id":244,"module_name":"","relation_id":45},{"task_name":"222","project_custom_id":337,"bean_name":"bean1529636998991","module_id":0,"end_time":1535644800000,"employee_name":"","id":242,"module_name":"","relation_id":3},{"task_name":"","project_custom_id":323,"bean_name":"","module_id":0,"end_time":1535596112465,"employee_name":"","id":228,"module_name":"","relation_id":0},{"task_name":"徐兵的子任务","project_custom_id":305,"bean_name":"","module_id":0,"end_time":1533744000000,"employee_name":"徐兵","id":213,"module_name":"","relation_id":0},{"task_name":"","project_custom_id":302,"bean_name":"","module_id":0,"end_time":1535096636557,"employee_name":"","id":210,"module_name":"","relation_id":0},{"task_name":"111","project_custom_id":301,"bean_name":"project_custom","module_id":0,"end_time":1535096002468,"employee_name":"","id":209,"module_name":"","relation_id":16},{"task_name":"","project_custom_id":300,"bean_name":"","module_id":0,"end_time":1535033270440,"employee_name":"","id":208,"module_name":"","relation_id":0},{"task_name":"","project_custom_id":299,"bean_name":"","module_id":0,"end_time":1535093340529,"employee_name":"","id":207,"module_name":"","relation_id":105},{"task_name":"223","project_custom_id":293,"bean_name":"project_custom","module_id":0,"end_time":1535558400000,"employee_name":"","id":202,"module_name":"","relation_id":81}],"pageInfo":{"totalPages":2,"pageSize":10,"totalRows":15,"pageNum":1,"curPageSize":10}}
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
         * dataList : [{"task_name":"","project_custom_id":340,"bean_name":"bean1529636406668","module_id":0,"end_time":1535702871449,"employee_name":"","id":245,"module_name":"","relation_id":3},{"task_name":"","project_custom_id":339,"bean_name":"project_custom","module_id":0,"end_time":1535702759087,"employee_name":"","id":244,"module_name":"","relation_id":45},{"task_name":"222","project_custom_id":337,"bean_name":"bean1529636998991","module_id":0,"end_time":1535644800000,"employee_name":"","id":242,"module_name":"","relation_id":3},{"task_name":"","project_custom_id":323,"bean_name":"","module_id":0,"end_time":1535596112465,"employee_name":"","id":228,"module_name":"","relation_id":0},{"task_name":"徐兵的子任务","project_custom_id":305,"bean_name":"","module_id":0,"end_time":1533744000000,"employee_name":"徐兵","id":213,"module_name":"","relation_id":0},{"task_name":"","project_custom_id":302,"bean_name":"","module_id":0,"end_time":1535096636557,"employee_name":"","id":210,"module_name":"","relation_id":0},{"task_name":"111","project_custom_id":301,"bean_name":"project_custom","module_id":0,"end_time":1535096002468,"employee_name":"","id":209,"module_name":"","relation_id":16},{"task_name":"","project_custom_id":300,"bean_name":"","module_id":0,"end_time":1535033270440,"employee_name":"","id":208,"module_name":"","relation_id":0},{"task_name":"","project_custom_id":299,"bean_name":"","module_id":0,"end_time":1535093340529,"employee_name":"","id":207,"module_name":"","relation_id":105},{"task_name":"223","project_custom_id":293,"bean_name":"project_custom","module_id":0,"end_time":1535558400000,"employee_name":"","id":202,"module_name":"","relation_id":81}]
         * pageInfo : {"totalPages":2,"pageSize":10,"totalRows":15,"pageNum":1,"curPageSize":10}
         */

        private PageInfo pageInfo;
        private List<DataListBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }


        public static class DataListBean {
            /**
             * task_name :
             * project_custom_id : 340
             * bean_name : bean1529636406668
             * module_id : 0
             * end_time : 1535702871449
             * employee_name :
             * id : 245
             * module_name :
             * relation_id : 3
             */

            private String task_name;
            private String project_custom_id;
            private String bean_name;
            private String module_id;
            private String end_time;
            private String employee_name;
            private String id;
            private String module_name;
            private String relation_id;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getTask_name() {
                return task_name;
            }

            public void setTask_name(String task_name) {
                this.task_name = task_name;
            }

            public String getProject_custom_id() {
                return project_custom_id;
            }

            public void setProject_custom_id(String project_custom_id) {
                this.project_custom_id = project_custom_id;
            }

            public String getBean_name() {
                return bean_name;
            }

            public void setBean_name(String bean_name) {
                this.bean_name = bean_name;
            }

            public String getModule_id() {
                return module_id;
            }

            public void setModule_id(String module_id) {
                this.module_id = module_id;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public String getRelation_id() {
                return relation_id;
            }

            public void setRelation_id(String relation_id) {
                this.relation_id = relation_id;
            }
        }
    }
}
