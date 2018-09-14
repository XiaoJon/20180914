package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/6/26.
 * Describe：
 */

public class WorkTimeBean {

    /**
     * name : 星期二
     * id : 21
     * type : 3
     * label : 班3-2:07:00-14:00;12:00-15:00;16:00-18:00
     * time : 1529995805
     */

    private String name;
    private String id;
    private String type;
    private String label;
    private String time;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
