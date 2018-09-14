package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class TaskMemberListResultBean extends BaseBean {

    /**
     * data : {"dataList":[{"project_role":"2","create_time":1529639519094,"project_task_role":"2","modify_time":"","read_status":"1","del_status":"0","task_id":333,"employee_name":"张琴","modify_by":"","type_status":"1","external_member":"","create_by":2,"active_status":"0","read_time":1529896450120,"project_id":51,"employee_id":2,"project_task_status":"0","employee_pic":"null","id":446,"father_id":-1,"employee_status":"0","project_member_id":446}]}
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
             * project_role : 2
             * create_time : 1529639519094
             * project_task_role : 2
             * modify_time :
             * read_status : 1
             * del_status : 0
             * task_id : 333
             * employee_name : 张琴
             * modify_by :
             * type_status : 1
             * external_member :
             * create_by : 2
             * active_status : 0
             * read_time : 1529896450120
             * project_id : 51
             * employee_id : 2
             * project_task_status : 0
             * employee_pic : null
             * id : 446
             * father_id : -1
             * employee_status : 0
             * project_member_id : 446
             */

            private String project_role;
            private String create_time;
            private String project_task_role;
            private String modify_time;
            private String read_status;
            private String del_status;
            private long task_id;
            private String employee_name;
            private String modify_by;
            private String type_status;
            private String external_member;
            private long create_by;
            private String active_status;
            private String read_time;
            private long project_id;
            private long employee_id;
            private String project_task_status;
            private String employee_pic;
            private long id;
            private long father_id;
            private String employee_status;
            private long project_member_id;

            public String getProject_role() {
                return project_role;
            }

            public void setProject_role(String project_role) {
                this.project_role = project_role;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getProject_task_role() {
                return project_task_role;
            }

            public void setProject_task_role(String project_task_role) {
                this.project_task_role = project_task_role;
            }

            public String getModify_time() {
                return modify_time;
            }

            public void setModify_time(String modify_time) {
                this.modify_time = modify_time;
            }

            public String getRead_status() {
                return read_status;
            }

            public void setRead_status(String read_status) {
                this.read_status = read_status;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public long getTask_id() {
                return task_id;
            }

            public void setTask_id(long task_id) {
                this.task_id = task_id;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getModify_by() {
                return modify_by;
            }

            public void setModify_by(String modify_by) {
                this.modify_by = modify_by;
            }

            public String getType_status() {
                return type_status;
            }

            public void setType_status(String type_status) {
                this.type_status = type_status;
            }

            public String getExternal_member() {
                return external_member;
            }

            public void setExternal_member(String external_member) {
                this.external_member = external_member;
            }

            public long getCreate_by() {
                return create_by;
            }

            public void setCreate_by(long create_by) {
                this.create_by = create_by;
            }

            public String getActive_status() {
                return active_status;
            }

            public void setActive_status(String active_status) {
                this.active_status = active_status;
            }

            public String getRead_time() {
                return read_time;
            }

            public void setRead_time(String read_time) {
                this.read_time = read_time;
            }

            public long getProject_id() {
                return project_id;
            }

            public void setProject_id(long project_id) {
                this.project_id = project_id;
            }

            public long getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(long employee_id) {
                this.employee_id = employee_id;
            }

            public String getProject_task_status() {
                return project_task_status;
            }

            public void setProject_task_status(String project_task_status) {
                this.project_task_status = project_task_status;
            }

            public String getEmployee_pic() {
                return employee_pic;
            }

            public void setEmployee_pic(String employee_pic) {
                this.employee_pic = employee_pic;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getFather_id() {
                return father_id;
            }

            public void setFather_id(long father_id) {
                this.father_id = father_id;
            }

            public String getEmployee_status() {
                return employee_status;
            }

            public void setEmployee_status(String employee_status) {
                this.employee_status = employee_status;
            }

            public long getProject_member_id() {
                return project_member_id;
            }

            public void setProject_member_id(long project_member_id) {
                this.project_member_id = project_member_id;
            }
        }
    }
}
