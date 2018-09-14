package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2018/6/25.
 */

public class QueryHierarchyResultBean extends BaseBean {

    /**
     * data : {"nodename":"项目流程","subnodename":"待处理","subnodeid":300,"taskname":"111","nodeid":144,"taskid":333}
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
         * nodename : 项目流程
         * subnodename : 待处理
         * subnodeid : 300
         * taskname : 111
         * nodeid : 144
         * taskid : 333
         */

        private String projectname;
        private String nodename;
        private String subnodename;
        private String subnodename2;
        private long subnodeid;
        private long subnodeid2;
        private String taskname;
        private long nodeid;
        private long taskid;

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
        }

        public String getNodename() {
            return nodename;
        }

        public void setNodename(String nodename) {
            this.nodename = nodename;
        }

        public String getSubnodename() {
            return subnodename;
        }

        public void setSubnodename(String subnodename) {
            this.subnodename = subnodename;
        }

        public String getSubnodename2() {
            return subnodename2;
        }

        public void setSubnodename2(String subnodename2) {
            this.subnodename2 = subnodename2;
        }

        public long getSubnodeid() {
            return subnodeid;
        }

        public void setSubnodeid(long subnodeid) {
            this.subnodeid = subnodeid;
        }

        public long getSubnodeid2() {
            return subnodeid2;
        }

        public void setSubnodeid2(long subnodeid2) {
            this.subnodeid2 = subnodeid2;
        }

        public String getTaskname() {
            return taskname;
        }

        public void setTaskname(String taskname) {
            this.taskname = taskname;
        }

        public long getNodeid() {
            return nodeid;
        }

        public void setNodeid(long nodeid) {
            this.nodeid = nodeid;
        }

        public long getTaskid() {
            return taskid;
        }

        public void setTaskid(long taskid) {
            this.taskid = taskid;
        }
    }
}
