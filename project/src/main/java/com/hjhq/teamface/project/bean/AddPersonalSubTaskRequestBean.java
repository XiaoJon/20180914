package com.hjhq.teamface.project.bean;

/**
 * 新增子任务
 *
 * @author Administrator
 * @date 2018/6/19
 */

public class AddPersonalSubTaskRequestBean {

    private String task_id;
    private String name;
    private String end_time;
    private String executor_id;
    private String project_custom_id;
    private String relation_data;
    private String relation_id;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(String executor_id) {
        this.executor_id = executor_id;
    }

    public String getProject_custom_id() {
        return project_custom_id;
    }

    public void setProject_custom_id(String project_custom_id) {
        this.project_custom_id = project_custom_id;
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
