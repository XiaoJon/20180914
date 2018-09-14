package com.hjhq.teamface.custom.bean;

import java.util.HashMap;

/**
 * @author Administrator
 * @date 2017/11/1
 */

public class RelationDataRequestBean {

    /**
     * bean : invoice
     * search_field : order_id
     * form : {"customer_id":"15","order_id":"6"}
     */

    private String bean;
    private String searchField;
    private String subform;
    private HashMap<String,Object> form;

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public HashMap<String, Object> getForm() {
        return form;
    }

    public void setForm(HashMap<String, Object> form) {
        this.form = form;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String subform) {
        this.subform = subform;
    }
}
