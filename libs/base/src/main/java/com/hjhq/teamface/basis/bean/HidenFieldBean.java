package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * 隐藏字段
 *
 * @author Administrator
 * @date 2018/5/31
 */

public class HidenFieldBean implements Serializable {
    private String name;
    private String label;

    public HidenFieldBean() {
    }

    public HidenFieldBean(String name, String label) {
        this.name = name;
        this.label = label;
    }

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
}
