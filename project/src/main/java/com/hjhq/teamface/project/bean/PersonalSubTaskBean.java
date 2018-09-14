package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.common.bean.ProjectLabelBean;

import java.util.List;

/**
 * 子任务
 *
 * @author Administrator
 * @date 2018/6/25
 */

public class PersonalSubTaskBean {


    /**
     * relation_data : 123
     * mutlipicklist_1533284858996_v :
     * datetime_1533284886764 :
     * phone_1533284829876 :
     * number_1533284837860 :
     * picklist_1533292413032 : [{"color":"#FFFFFF","label":"选项2","value":"1"},{"color":"#FFFFFF","label":"选项3","value":"2"}]
     * project_custom_id : 89
     * datetime_deadline :
     * datetime_modify_time :
     * activate_number : 0
     * location_1533285026436 : {"area":"广东省#深圳市#南山区","lng":113.93981,"value":"广东省深圳市南山区粤海街道高新南十道23号深圳市软件产业基地","lat":22.52443}
     * personnel_modify_by :
     * id : 27
     * picklist_1533292413032_v : 1,2
     * complete_status : 0
     * sort :
     * passed_status : 0
     * participants_only : 0
     * textarea_1533284916258 :
     * name : 111
     * multitext_1533284863629 :
     * executor_id : [{"employee_name":"杨建","picture":"","executor_id":10}]
     * reference_1533291257031 : [{"name":"","id":""}]
     * attachment_1533284901331 :
     * picklist_1533284804030 : [{"color":"#FFFFFF","label":"选项2","value":"1"}]
     * reference_relation :
     * task_id : 79
     * del_status : 0
     * mutlipicklist_1533284858996 :
     * picture_1533284811864 :
     * bean_name : bean1532509086102
     * picklist_tag_v :
     * picklist_1533284804030_v : 1
     * picklist_tag : [{"colour":"#FF1D32","name":"紧急","id":4},{"colour":"#000000","name":"Black","id":16},{"colour":"#5821A7","name":"Purple","id":20},{"colour":"#3057E5","name":"一般","id":3},{"colour":"#FF1D32","name":"Red","id":15},{"colour":"#73B32D","name":"轻微","id":2},{"colour":"#F57221","name":"Orange","id":18},{"colour":"#73B32D","name":"Green","id":17}]
     * url_1533284814908 :
     * area_1533284869909 :
     * personnel_create_by : 12
     * end_time : 1535644800000
     * text_1533284914213 :
     * multitext_desc : <p>我是关联的新建任务</p>
     * datetime_starttime :
     * employee_name : 杨建
     * picture :
     * relation_id : 19
     * datetime_create_time : 1533526921638
     * personnel_execution : [{"post_name":"","name":"洪","id":27,"picture":""}]
     * text_name : 我是关联的新建任务
     * multi_1533284844699_v :
     * multi_1533284844699 :
     * personnel_1533284879004 : []
     */

    private String relation_data;
    private String project_custom_id;
    private String datetime_deadline;
    private String datetime_modify_time;
    private String activate_number;
    private String personnel_modify_by;
    private Long id;
    private String complete_status;
    private String sort;
    private String passed_status;
    private String participants_only;
    private String name;
    private String reference_relation;
    private Long task_id;
    private String del_status;
    private String bean_name;
    private String picklist_tag_v;
    private String end_time;
    private String multitext_desc;
    private String datetime_starttime;
    private String employee_name;
    private String picture;
    private String relation_id;
    private String datetime_create_time;
    private String text_name;
    private List<ExecutorIdBean> executor_id;
    private List<ProjectLabelBean> picklist_tag;
    private List<Member> personnel_execution;


    public String getRelation_data() {
        return relation_data;
    }

    public void setRelation_data(String relation_data) {
        this.relation_data = relation_data;
    }

    public String getProject_custom_id() {
        return project_custom_id;
    }

    public void setProject_custom_id(String project_custom_id) {
        this.project_custom_id = project_custom_id;
    }

    public String getDatetime_deadline() {
        return datetime_deadline;
    }

    public void setDatetime_deadline(String datetime_deadline) {
        this.datetime_deadline = datetime_deadline;
    }

    public String getDatetime_modify_time() {
        return datetime_modify_time;
    }

    public void setDatetime_modify_time(String datetime_modify_time) {
        this.datetime_modify_time = datetime_modify_time;
    }

    public String getActivate_number() {
        return activate_number;
    }

    public void setActivate_number(String activate_number) {
        this.activate_number = activate_number;
    }

    public String getPersonnel_modify_by() {
        return personnel_modify_by;
    }

    public void setPersonnel_modify_by(String personnel_modify_by) {
        this.personnel_modify_by = personnel_modify_by;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplete_status() {
        return complete_status;
    }

    public void setComplete_status(String complete_status) {
        this.complete_status = complete_status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPassed_status() {
        return passed_status;
    }

    public void setPassed_status(String passed_status) {
        this.passed_status = passed_status;
    }

    public String getParticipants_only() {
        return participants_only;
    }

    public void setParticipants_only(String participants_only) {
        this.participants_only = participants_only;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference_relation() {
        return reference_relation;
    }

    public void setReference_relation(String reference_relation) {
        this.reference_relation = reference_relation;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getBean_name() {
        return bean_name;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public String getPicklist_tag_v() {
        return picklist_tag_v;
    }

    public void setPicklist_tag_v(String picklist_tag_v) {
        this.picklist_tag_v = picklist_tag_v;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getMultitext_desc() {
        return multitext_desc;
    }

    public void setMultitext_desc(String multitext_desc) {
        this.multitext_desc = multitext_desc;
    }

    public String getDatetime_starttime() {
        return datetime_starttime;
    }

    public void setDatetime_starttime(String datetime_starttime) {
        this.datetime_starttime = datetime_starttime;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getDatetime_create_time() {
        return datetime_create_time;
    }

    public void setDatetime_create_time(String datetime_create_time) {
        this.datetime_create_time = datetime_create_time;
    }

    public String getText_name() {
        return text_name;
    }

    public void setText_name(String text_name) {
        this.text_name = text_name;
    }

    public List<ExecutorIdBean> getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(List<ExecutorIdBean> executor_id) {
        this.executor_id = executor_id;
    }

    public List<ProjectLabelBean> getPicklist_tag() {
        return picklist_tag;
    }

    public void setPicklist_tag(List<ProjectLabelBean> picklist_tag) {
        this.picklist_tag = picklist_tag;
    }

    public List<Member> getPersonnel_execution() {
        return personnel_execution;
    }

    public void setPersonnel_execution(List<Member> personnel_execution) {
        this.personnel_execution = personnel_execution;
    }

    public static class ExecutorIdBean {
        /**
         * employee_name : 杨建
         * picture :
         * executor_id : 10
         */

        private String employee_name;
        private String picture;
        private Long executor_id;

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public Long getExecutor_id() {
            return executor_id;
        }

        public void setExecutor_id(Long executor_id) {
            this.executor_id = executor_id;
        }
    }


}
