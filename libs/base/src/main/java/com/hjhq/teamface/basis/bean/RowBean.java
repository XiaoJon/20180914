package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * Created by lx on 2017/9/22.
 */

public class RowBean implements Serializable {
    /**
     * name : id
     * label : 主键id
     * value : 2
     */

    private String name;
    private String label;
    private String value;
    //判断是否是第一行
    private String isLock;
    //时间格式
    private String other;
    private String hidden;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
}
