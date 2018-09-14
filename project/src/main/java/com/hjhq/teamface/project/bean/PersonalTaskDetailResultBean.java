package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/25.
 */

public class PersonalTaskDetailResultBean extends BaseBean {

    /**
     * data : {"task_name":"111","associatesArr":[],"relationArr2":[],"bean_type":"2","modify_time":2,"description":"","del_status":"0","modify_by":1529890116990,"quote_status":"0","copy_task_id":"","create_by":2,"bean_name":"project_custom_51","complete_number":0,"project_id":51,"relationArr":[{"module_id":null,"dataType":3,"taskInfoId":111,"completeStatus":0,"beanName":"bean1527849760428","row":[{"name":"text_1523851995489","label":"名称","value":"1"},{"name":"area_1527849768164","label":"省市区","value":"120000:天津市,120100:天津市,120103:河西区"},{"name":"datetime_create_time","label":"创建时间","value":1529640500707},{"name":"datetime_modify_time","label":"修改时间","value":1529640500707},{"name":"id","label":"主键id","value":18},{"name":"del_status","label":"删除状态","value":"0"},{"name":"seas_pool_id","label":"公海池编号","value":0},{"name":"number_1527849821642","label":"销售额","value":1},{"name":"number_1527849823031","label":"价格","value":1},{"name":"reference_1528769680958","label":"关联关系","value":""},{"name":"personnel_principal","label":"负责人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]},{"name":"personnel_create_by","label":"创建人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]},{"name":"personnel_modify_by","label":"修改人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]}],"module_name":"","beanId":18}],"copy_status":"0","id":333,"task_status":"","finished_time":"","create_time":1529639518066,"sub_id":300,"associates_status":"1","personnel_collaboratives":"","end_time":1530201600000,"shareArr":[],"complete_status":"0","employee_name":"张琴","sort":333,"check_member":2,"check_status":"1","passed_status":"0","relation_id":1,"module_id":"","subTaskArr":[],"quote_task_id":"","job_id":"","employee_pic":"null","module_name":"任务","join_status":"0","customArr":{"personnel_create_by":"2","reference_relation":"","multitext_desc":"<p>套二厅二套日<\/p><p><br/><\/p>","del_status":"0","datetime_deadline":1530201600000,"datetime_modify_time":1529890115787,"picklist_tag_v":"","datetime_create_time":1529639516905,"personnel_execution":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}],"personnel_modify_by":"2","text_name":"111","attachment_1520248017179":[],"seas_pool_id":0,"picklist_tag":"5,8,14,12,3","beanName":"project_custom_51","id":1,"beanId":1},"label_id":"","executor_id":2}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        /**
         * relation_data : Teamface
         * personnel_create_by : 22
         * del_status : 0
         * complete_status : 0
         * check_member :
         * check_status :
         * relation_id : 27
         * project_custom_id : 88
         * datetime_modify_time :
         * activate_number : 0
         * bean_name : project_custom
         * datetime_create_time : 1533519488952
         * participants_only : 0
         * fromType : 0
         * personnel_modify_by :
         * employee_id : 22
         * name : 我是新增个人任务
         * customLayout : {"reference_1533291257031":[{"name":"","id":""}],"attachment_1533284901331":"","picklist_1533284804030":[{"color":"#FFFFFF","label":"选项2","value":"1"}],"mutlipicklist_1533284858996_v":"","reference_relation":"","datetime_1533284886764":"","mutlipicklist_1533284858996":"","phone_1533284829876":"","number_1533284837860":"","picture_1533284811864":"","picklist_1533292413032":[{"color":"#FFFFFF","label":"选项2","value":"1"},{"color":"#FFFFFF","label":"选项3","value":"2"}],"datetime_deadline":1533830400000,"picklist_tag_v":"","location_1533285026436":{"area":"广东省#深圳市#南山区","lng":113.93981,"value":"广东省深圳市南山区粤海街道高新南十道23号深圳市软件产业基地","lat":22.52443},"picklist_1533284804030_v":"1","picklist_tag":[{"colour":"#3057E5","name":"一般","id":3},{"colour":"#FF1D32","name":"紧急","id":4},{"colour":"#000000","name":"Black","id":16},{"colour":"#5821A7","name":"Purple","id":20},{"colour":"#73B32D","name":"Green","id":17},{"colour":"#73B32D","name":"轻微","id":2},{"colour":"#F57221","name":"Orange","id":18}],"id":88,"url_1533284814908":"","area_1533284869909":"","picklist_1533292413032_v":"1,2","text_1533284914213":"","multitext_desc":"<p>我是新增个人任务<\/p>","datetime_starttime":1533398400000,"personnel_execution":[{"post_name":"","name":"洪","id":27,"picture":""}],"textarea_1533284916258":"","text_name":"我是新增个人任务","attachment_customnumber":[],"multi_1533284844699_v":"","multi_1533284844699":"","personnel_1533284879004":[],"multitext_1533284863629":""}
         * id : 78
         * repeat_status : 0
         */

        private String relation_data;
        private String personnel_create_by;
        private String del_status;
        private String complete_status;
        private String check_member;
        private String check_status;
        private String relation_id;
        private String project_custom_id;
        private String datetime_modify_time;
        private String activate_number;
        private String bean_name;
        private String datetime_create_time;
        private String participants_only;
        private String fromType;
        private String from_status;
        private String personnel_modify_by;
        private String employee_id;
        private String name;
        private Map<String,Object> customLayout;
        private String id;
        private String repeat_status;

        public String getRelation_data() {
            return relation_data;
        }

        public void setRelation_data(String relation_data) {
            this.relation_data = relation_data;
        }

        public String getPersonnel_create_by() {
            return personnel_create_by;
        }

        public void setPersonnel_create_by(String personnel_create_by) {
            this.personnel_create_by = personnel_create_by;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getComplete_status() {
            return complete_status;
        }

        public void setComplete_status(String complete_status) {
            this.complete_status = complete_status;
        }

        public String getCheck_member() {
            return check_member;
        }

        public void setCheck_member(String check_member) {
            this.check_member = check_member;
        }

        public String getCheck_status() {
            return check_status;
        }

        public void setCheck_status(String check_status) {
            this.check_status = check_status;
        }

        public String getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(String relation_id) {
            this.relation_id = relation_id;
        }

        public String getProject_custom_id() {
            return project_custom_id;
        }

        public void setProject_custom_id(String project_custom_id) {
            this.project_custom_id = project_custom_id;
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

        public String getBean_name() {
            return bean_name;
        }

        public void setBean_name(String bean_name) {
            this.bean_name = bean_name;
        }

        public String getDatetime_create_time() {
            return datetime_create_time;
        }

        public void setDatetime_create_time(String datetime_create_time) {
            this.datetime_create_time = datetime_create_time;
        }

        public String getParticipants_only() {
            return participants_only;
        }

        public void setParticipants_only(String participants_only) {
            this.participants_only = participants_only;
        }

        public String getFromType() {
            return fromType;
        }

        public void setFromType(String fromType) {
            this.fromType = fromType;
        }

        public String getFrom_status() {
            return from_status;
        }

        public void setFrom_status(String from_status) {
            this.from_status = from_status;
        }

        public String getPersonnel_modify_by() {
            return personnel_modify_by;
        }

        public void setPersonnel_modify_by(String personnel_modify_by) {
            this.personnel_modify_by = personnel_modify_by;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Object> getCustomLayout() {
            return customLayout;
        }

        public void setCustomLayout(Map<String, Object> customLayout) {
            this.customLayout = customLayout;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRepeat_status() {
            return repeat_status;
        }

        public void setRepeat_status(String repeat_status) {
            this.repeat_status = repeat_status;
        }
    }
}
