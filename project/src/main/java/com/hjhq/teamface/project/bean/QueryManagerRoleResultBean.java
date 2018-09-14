package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class QueryManagerRoleResultBean extends BaseBean {

    /**
     * data : {"priviledge":{"create_by":1,"update_time":"","project_role":"1","priviledge_ids":"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24","role_type":"1","create_time":1523259757802,"describle":"拥有当前负责项目所有权限，可修改权限范围","name":"项目负责人","del_status":"0","id":1,"update_by":"","priviledgeList":[{"name":"修改项目信息","id":1,"status":1},{"name":"设置项目偏好","id":2,"status":1},{"name":"设置项目状态变更","id":3,"status":1},{"name":"添加标签","id":4,"status":1},{"name":"移除标签","id":5,"status":1},{"name":"导出项目任务","id":6,"status":1},{"name":"邀请成员","id":7,"status":1},{"name":"移除成员","id":8,"status":1},{"name":"添加任务分组","id":9,"status":1},{"name":"编辑任务分组","id":10,"status":1},{"name":"删除任务分组","id":11,"status":1},{"name":"移动任务分组","id":12,"status":1},{"name":"新建列表","id":13,"status":1},{"name":"编辑列表名称","id":14,"status":1},{"name":"删除列表","id":15,"status":1},{"name":"移动列表","id":16,"status":1},{"name":"任务批量操作","id":17,"status":1},{"name":"新建任务","id":18,"status":1},{"name":"设置任务提醒","id":19,"status":1},{"name":"点赞","id":20,"status":1},{"name":"允许评论","id":21,"status":1},{"name":"添加文件夹","id":22,"status":1},{"name":"编辑文件夹","id":23,"status":1},{"name":"删除文件","id":24,"status":1}]}}
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
         * priviledge : {"create_by":1,"update_time":"","project_role":"1","priviledge_ids":"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24","role_type":"1","create_time":1523259757802,"describle":"拥有当前负责项目所有权限，可修改权限范围","name":"项目负责人","del_status":"0","id":1,"update_by":"","priviledgeList":[{"name":"修改项目信息","id":1,"status":1},{"name":"设置项目偏好","id":2,"status":1},{"name":"设置项目状态变更","id":3,"status":1},{"name":"添加标签","id":4,"status":1},{"name":"移除标签","id":5,"status":1},{"name":"导出项目任务","id":6,"status":1},{"name":"邀请成员","id":7,"status":1},{"name":"移除成员","id":8,"status":1},{"name":"添加任务分组","id":9,"status":1},{"name":"编辑任务分组","id":10,"status":1},{"name":"删除任务分组","id":11,"status":1},{"name":"移动任务分组","id":12,"status":1},{"name":"新建列表","id":13,"status":1},{"name":"编辑列表名称","id":14,"status":1},{"name":"删除列表","id":15,"status":1},{"name":"移动列表","id":16,"status":1},{"name":"任务批量操作","id":17,"status":1},{"name":"新建任务","id":18,"status":1},{"name":"设置任务提醒","id":19,"status":1},{"name":"点赞","id":20,"status":1},{"name":"允许评论","id":21,"status":1},{"name":"添加文件夹","id":22,"status":1},{"name":"编辑文件夹","id":23,"status":1},{"name":"删除文件","id":24,"status":1}]}
         */

        private PriviledgeBean priviledge;

        public PriviledgeBean getPriviledge() {
            return priviledge;
        }

        public void setPriviledge(PriviledgeBean priviledge) {
            this.priviledge = priviledge;
        }

        public static class PriviledgeBean {
            /**
             * create_by : 1
             * update_time :
             * project_role : 1
             * priviledge_ids : 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24
             * role_type : 1
             * create_time : 1523259757802
             * describle : 拥有当前负责项目所有权限，可修改权限范围
             * name : 项目负责人
             * del_status : 0
             * id : 1
             * update_by :
             * priviledgeList : [{"name":"修改项目信息","id":1,"status":1},{"name":"设置项目偏好","id":2,"status":1},{"name":"设置项目状态变更","id":3,"status":1},{"name":"添加标签","id":4,"status":1},{"name":"移除标签","id":5,"status":1},{"name":"导出项目任务","id":6,"status":1},{"name":"邀请成员","id":7,"status":1},{"name":"移除成员","id":8,"status":1},{"name":"添加任务分组","id":9,"status":1},{"name":"编辑任务分组","id":10,"status":1},{"name":"删除任务分组","id":11,"status":1},{"name":"移动任务分组","id":12,"status":1},{"name":"新建列表","id":13,"status":1},{"name":"编辑列表名称","id":14,"status":1},{"name":"删除列表","id":15,"status":1},{"name":"移动列表","id":16,"status":1},{"name":"任务批量操作","id":17,"status":1},{"name":"新建任务","id":18,"status":1},{"name":"设置任务提醒","id":19,"status":1},{"name":"点赞","id":20,"status":1},{"name":"允许评论","id":21,"status":1},{"name":"添加文件夹","id":22,"status":1},{"name":"编辑文件夹","id":23,"status":1},{"name":"删除文件","id":24,"status":1}]
             */

            private long create_by;
            private String update_time;
            private String project_role;
            private String project_task_role;
            private String priviledge_ids;
            private String role_type;
            private String create_time;
            private String describle;
            private String name;
            private String del_status;
            private long id;
            private String update_by;
            private List<PriviledgeListBean> priviledgeList;

            public long getCreate_by() {
                return create_by;
            }

            public void setCreate_by(long create_by) {
                this.create_by = create_by;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

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

            public String getPriviledge_ids() {
                return priviledge_ids;
            }

            public void setPriviledge_ids(String priviledge_ids) {
                this.priviledge_ids = priviledge_ids;
            }

            public String getRole_type() {
                return role_type;
            }

            public void setRole_type(String role_type) {
                this.role_type = role_type;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getDescrible() {
                return describle;
            }

            public void setDescrible(String describle) {
                this.describle = describle;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getUpdate_by() {
                return update_by;
            }

            public void setUpdate_by(String update_by) {
                this.update_by = update_by;
            }

            public List<PriviledgeListBean> getPriviledgeList() {
                return priviledgeList;
            }

            public void setPriviledgeList(List<PriviledgeListBean> priviledgeList) {
                this.priviledgeList = priviledgeList;
            }

            public static class PriviledgeListBean {
                /**
                 * name : 修改项目信息
                 * id : 1
                 * status : 1
                 */

                private String name;
                private long id;
                private int status;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }
        }
    }
}
