package com.hjhq.teamface.project.bean;

import java.util.List;

/**
 * 节点拖拽请求实体
 * Created by Administrator on 2018/4/26.
 */

public class SortNodeRequestBean {
    private long projectId;
    private long toNodeId;
    private long activeNodeId;
    private long originalNodeId;
    private List<NodeBean> dataList;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getToNodeId() {
        return toNodeId;
    }

    public long getActiveNodeId() {
        return activeNodeId;
    }

    public void setActiveNodeId(long activeNodeId) {
        this.activeNodeId = activeNodeId;
    }

    public long getOriginalNodeId() {
        return originalNodeId;
    }

    public void setOriginalNodeId(long originalNodeId) {
        this.originalNodeId = originalNodeId;
    }

    public void setToNodeId(long toNodeId) {
        this.toNodeId = toNodeId;
    }

    public List<NodeBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<NodeBean> dataList) {
        this.dataList = dataList;
    }
}
