package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/7/18.
 * Describe：
 */

public class AddRelevantBean {

    /**
     * projectId : 1
     * relation_id : 2
     * module_id : 2
     * module_name : memo
     * bean_name : 1
     * bean_type : 22
     * share_id : 22
     */

    private Long projectId;
    private String relation_id;
    private Long module_id;
    private String module_name;
    //
    private String bean_name;
    private Long bean_type;
    private Long share_id;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public Long getModule_id() {
        return module_id;
    }

    public void setModule_id(Long module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getBean_name() {
        return bean_name;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public Long getBean_type() {
        return bean_type;
    }

    public void setBean_type(Long bean_type) {
        this.bean_type = bean_type;
    }

    public Long getShare_id() {
        return share_id;
    }

    public void setShare_id(Long share_id) {
        this.share_id = share_id;
    }
}
