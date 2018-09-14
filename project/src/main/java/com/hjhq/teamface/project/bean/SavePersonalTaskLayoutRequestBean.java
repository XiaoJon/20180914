package com.hjhq.teamface.project.bean;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/22.
 */

public class SavePersonalTaskLayoutRequestBean {

    /**
     * bean : bean1534489456312
     * data : {"fieldName1":"字段值1","fieldName2":"字段值2"}
     */

    private long id;
    private String bean_name;
    private String name;
    private Map<String,Object> customLayout;
    private String participants_only;
    private String relation_data;
    private String relation_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBean_name() {
        return bean_name;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParticipants_only() {
        return participants_only;
    }

    public void setParticipants_only(String participants_only) {
        this.participants_only = participants_only;
    }

    public Map<String, Object> getCustomLayout() {
        return customLayout;
    }

    public void setCustomLayout(Map<String, Object> customLayout) {
        this.customLayout = customLayout;
    }

    public String getRelation_data() {
        return relation_data;
    }

    public void setRelation_data(String relation_data) {
        this.relation_data = relation_data;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }
}
