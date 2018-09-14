package com.hjhq.teamface.project.bean;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/22.
 */

public class SaveTaskLayoutRequestBean {

    /**
     * bean : bean1534489456312
     * data : {"fieldName1":"字段值1","fieldName2":"字段值2"}
     */

    private String id;
    private String bean;
    private Map<String,Object> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
