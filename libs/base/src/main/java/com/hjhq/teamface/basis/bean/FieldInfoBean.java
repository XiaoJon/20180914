package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

public class FieldInfoBean implements Serializable {
    /**
     * id  : 2
     * field_label : 字段名
     * field_value : 1
     */

    private String id;
    private String field_label;
    private String field_value;
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField_label() {
        return field_label;
    }

    public void setField_label(String field_label) {
        this.field_label = field_label;
    }

    public String getField_value() {
        return field_value;
    }

    public void setField_value(String field_value) {
        this.field_value = field_value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}