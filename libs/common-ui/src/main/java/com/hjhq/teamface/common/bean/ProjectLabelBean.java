package com.hjhq.teamface.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目标签
 *
 * @author Administrator
 */
public class ProjectLabelBean implements Serializable {
    /**
     * colour : #ffea10
     * parent_id : 1
     * name : 竞争产品
     * parent_name : 竞争产品
     * id : 3
     * sequence_no : 1
     */

    private String colour;
    private String parent_id;
    private String name;
    private String id;
    private String sequence_no;
    private String parent_name;
    private List<ProjectLabelBean> childList;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void switchCheck() {
        this.check = !check;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(String sequence_no) {
        this.sequence_no = sequence_no;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public List<ProjectLabelBean> getChildList() {
        return childList;
    }

    public void setChildList(List<ProjectLabelBean> childList) {
        this.childList = childList;
    }
}