package com.hjhq.teamface.project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 节点实体
 *
 * @author Administrator
 * @date 2018/4/25
 */

public class NodeBean implements Serializable {
    private long project_id;
    private long main_id;
    private String name;
    private long id;

    private List<NodeBean> subnodeArr;
    private String children_data_type;
    private boolean isCheck;

    public NodeBean() {
    }

    public NodeBean(long project_id, long main_id) {
        this.project_id = project_id;
        this.main_id = main_id;
    }

    public NodeBean(long project_id, long main_id, long id) {
        this.project_id = project_id;
        this.main_id = main_id;
        this.id = id;
    }

    public NodeBean(long project_id, long main_id, long id, String name) {
        this.project_id = project_id;
        this.main_id = main_id;
        this.id = id;
        this.name = name;
    }



    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public long getMain_id() {
        return main_id;
    }

    public void setMain_id(long main_id) {
        this.main_id = main_id;
    }

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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<NodeBean> getSubnodeArr() {
        return subnodeArr;
    }

    public void setSubnodeArr(List<NodeBean> subnodeArr) {
        this.subnodeArr = subnodeArr;
    }

    public String getChildren_data_type() {
        return children_data_type;
    }

    public void setChildren_data_type(String children_data_type) {
        this.children_data_type = children_data_type;
    }
}
