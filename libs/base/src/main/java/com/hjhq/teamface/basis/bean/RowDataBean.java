package com.hjhq.teamface.basis.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by lx on 2017/9/22.
 */

public class RowDataBean implements Serializable {
    /**
     * name : id
     * label : 主键id
     * value : 2
     */

    private String name;
    private String label;
    private Object value;
    private String stringValue;
    //判断是否是第一行
    private String isLock;
    //时间格式
    private String other;
    private String hidden;

    public void setValue(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        return JSONObject.toJSONString(value);
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
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

    public Object getValue() {
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
