package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 下拉选项 bean
 *
 * @author Administrator
 * @date 2017/11/30
 */

public class EntryBean implements Serializable {
    /**
     * value : #value#
     * label : #label#
     * color : #color#
     */

    private String value;
    private String label;
    private String color;
    private List<EntryBean> subList;
    private boolean isCheck;
    //依赖的选择组件字段
    private String controlField;
    //依赖的选择组件值
    private List<EntryBean> relyonList;
    //需要隐藏的组件
    private List<HidenFieldBean> hidenFields;

    public EntryBean() {
    }

    public EntryBean(String value, String label, String color) {
        this.value = value;
        this.label = label;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<EntryBean> getSubList() {
        return subList;
    }

    public void setSubList(List<EntryBean> subList) {
        this.subList = subList;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getControlField() {
        return controlField;
    }

    public void setControlField(String controlField) {
        this.controlField = controlField;
    }

    public List<EntryBean> getRelyonList() {
        return relyonList;
    }

    public void setRelyonList(List<EntryBean> relyonList) {
        this.relyonList = relyonList;
    }

    public List<HidenFieldBean> getHidenFields() {
        return hidenFields;
    }

    public void setHidenFields(List<HidenFieldBean> hidenFields) {
        this.hidenFields = hidenFields;
    }
}
