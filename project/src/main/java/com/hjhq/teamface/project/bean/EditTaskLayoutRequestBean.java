package com.hjhq.teamface.project.bean;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/22.
 */

public class EditTaskLayoutRequestBean {

    /**
     * bean : bean1534489456312
     * data : {"fieldName1":"字段值1","fieldName2":"字段值2"}
     */

    private long id;
    private String bean;
    //1、主任务，2子任务
    private long task_id;
    private long type_status;
    private Map<String,Object> data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public long getType_status() {
        return type_status;
    }

    public void setType_status(long type_status) {
        this.type_status = type_status;
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
