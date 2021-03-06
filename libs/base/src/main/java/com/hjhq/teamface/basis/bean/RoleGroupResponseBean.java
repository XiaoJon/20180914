package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * 角色
 * Created by Administrator on 2018/1/16.
 */

public class RoleGroupResponseBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roles : [{"name":"企业所有者","remark":"","id":1,"role_group_id":1,"status":"0"},{"name":"系统管理员","remark":"","id":2,"role_group_id":1,"status":"0"},{"name":"成员","remark":"","id":3,"role_group_id":1,"status":"0"}]
         * name : 系统类角色
         * id : 1
         * sys_group : 1
         * status : 0
         */

        private String name;
        private int id;
        private int sys_group;
        private String status;
        private List<RolesBean> roles;
        private boolean isCheck;
        //是否参与选择,用于不可更改该或不能参与的成员条目
        private int selectState;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSys_group() {
            return sys_group;
        }

        public void setSys_group(int sys_group) {
            this.sys_group = sys_group;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<RolesBean> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesBean> roles) {
            this.roles = roles;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public int getSelectState() {
            return selectState;
        }

        public void setSelectState(int selectState) {
            this.selectState = selectState;
        }

        public static class RolesBean extends Member {
            /**
             * name : 企业所有者
             * remark :
             * id : 1
             * role_group_id : 1
             * status : 0
             */

            private String remark;
            private int role_group_id;
            private String status;

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }


            public int getRole_group_id() {
                return role_group_id;
            }

            public void setRole_group_id(int role_group_id) {
                this.role_group_id = role_group_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }
    }
}
