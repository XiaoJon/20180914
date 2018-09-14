package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 项目成员
 * Created by Administrator on 2018/7/4.
 */

public class ProjectMemberResultBean extends BaseBean {


    /**
     * data : {"dataList":[{"project_role":"1","create_time":1530699783558,"is_enable":"1","project_task_role":"0","modify_time":"","read_status":"0","del_status":"0","task_id":"","employee_name":"张琴","modify_by":"","type_status":"0","external_member":"","create_by":"","active_status":"0","read_time":"","project_id":75,"employee_id":2,"project_task_status":"0","employee_pic":"http://192.168.1.9:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=201807041852450.jpg&fileSize=47523","id":986,"father_id":"","project_member_id":986}]}
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
             * project_role : 1
             * create_time : 1530699783558
             * is_enable : 1
             * project_task_role : 0
             * modify_time :
             * read_status : 0
             * del_status : 0
             * task_id :
             * employee_name : 张琴
             * modify_by :
             * type_status : 0
             * external_member :
             * create_by :
             * active_status : 0
             * read_time :
             * project_id : 75
             * employee_id : 2
             * project_task_status : 0
             * employee_pic : http://192.168.1.9:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=201807041852450.jpg&fileSize=47523
             * id : 986
             * father_id :
             * project_member_id : 986
             */

            //1负责人 2协作人3访客
            private String project_role;
            private String project_task_role;
            private String employee_name;
            private String modify_by;
            private String type_status;
            private String external_member;
            //该角色激活状态  0否 1是
            private String is_enable;
            private String task_id;
            private long project_id;
            private long employee_id;
            private String project_task_status;
            private String employee_pic;
            private long id;
            private String project_member_id;

            public String getProject_role() {
                return project_role;
            }

            public void setProject_role(String project_role) {
                this.project_role = project_role;
            }

            public String getProject_task_role() {
                return project_task_role;
            }

            public void setProject_task_role(String project_task_role) {
                this.project_task_role = project_task_role;
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

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
            }

            public String getTask_id() {
                return task_id;
            }

            public void setTask_id(String task_id) {
                this.task_id = task_id;
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

            public String getProject_member_id() {
                return project_member_id;
            }

            public void setProject_member_id(String project_member_id) {
                this.project_member_id = project_member_id;
            }
        }
    }
}
