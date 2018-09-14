package com.hjhq.teamface.project.bean;

import java.util.List;

/**
 * 保存子节点
 * Created by Administrator on 2018/4/23.
 */

public class SaveSubNodeRequestBean {
    private long projectId;
    private long nodeId;
    private List<SubnodeArrBean> subnodeArr;

    public static class SubnodeArrBean {
        public SubnodeArrBean() {
        }

        public SubnodeArrBean(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public List<SubnodeArrBean> getSubnodeArr() {
        return subnodeArr;
    }

    public void setSubnodeArr(List<SubnodeArrBean> subnodeArr) {
        this.subnodeArr = subnodeArr;
    }
}
