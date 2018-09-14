package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 引用任务列表
 * Created by Administrator on 2018/6/21.
 */

public class ReferTaskListResultBean extends BaseBean {
    private DataBean data;

    public static class DataBean {
        private List<DataListBean> dataList;
        private PageInfo pageInfo;


        public static class DataListBean implements Serializable{
            //'执行人名称';
            private String employee_name;
            //'任务名称';
            private String task_name;
            //'截止时间 ';
            private String end_time;
            //'主任务ID';
            private long id;
            //'自定义引用id’
            private long relation_id;
            //'bean名称';
            private String bean_name;
            //'模块名称';
            private String module_name;
            //'列表ID';
            private long sub_id;
            //'项目id';
            private long project_id;

            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getTask_name() {
                return task_name;
            }

            public void setTask_name(String task_name) {
                this.task_name = task_name;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getRelation_id() {
                return relation_id;
            }

            public void setRelation_id(long relation_id) {
                this.relation_id = relation_id;
            }

            public String getBean_name() {
                return bean_name;
            }

            public void setBean_name(String bean_name) {
                this.bean_name = bean_name;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public long getSub_id() {
                return sub_id;
            }

            public void setSub_id(long sub_id) {
                this.sub_id = sub_id;
            }

            public long getProject_id() {
                return project_id;
            }

            public void setProject_id(long project_id) {
                this.project_id = project_id;
            }
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
