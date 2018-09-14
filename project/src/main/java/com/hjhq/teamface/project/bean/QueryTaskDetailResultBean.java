package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/25.
 */

public class QueryTaskDetailResultBean extends BaseBean {

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
         * task_name : 111
         * associatesArr : []
         * relationArr2 : []
         * bean_type : 2
         * modify_time : 2
         * description :
         * del_status : 0
         * modify_by : 1529890116990
         * quote_status : 0
         * copy_task_id :
         * create_by : 2
         * bean_name : project_custom_51
         * complete_number : 0
         * project_id : 51
         * relationArr : [{"module_id":null,"dataType":3,"taskInfoId":111,"completeStatus":0,"beanName":"bean1527849760428","row":[{"name":"text_1523851995489","label":"名称","value":"1"},{"name":"area_1527849768164","label":"省市区","value":"120000:天津市,120100:天津市,120103:河西区"},{"name":"datetime_create_time","label":"创建时间","value":1529640500707},{"name":"datetime_modify_time","label":"修改时间","value":1529640500707},{"name":"id","label":"主键id","value":18},{"name":"del_status","label":"删除状态","value":"0"},{"name":"seas_pool_id","label":"公海池编号","value":0},{"name":"number_1527849821642","label":"销售额","value":1},{"name":"number_1527849823031","label":"价格","value":1},{"name":"reference_1528769680958","label":"关联关系","value":""},{"name":"personnel_principal","label":"负责人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]},{"name":"personnel_create_by","label":"创建人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]},{"name":"personnel_modify_by","label":"修改人","value":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}]}],"module_name":"","beanId":18}]
         * copy_status : 0
         * id : 333
         * task_status :
         * finished_time :
         * create_time : 1529639518066
         * sub_id : 300
         * associates_status : 1
         * personnel_collaboratives :
         * end_time : 1530201600000
         * shareArr : []
         * complete_status : 0
         * employee_name : 张琴
         * sort : 333
         * check_member : 2
         * check_status : 1
         * passed_status : 0
         * relation_id : 1
         * module_id :
         * subTaskArr : []
         * quote_task_id :
         * job_id :
         * employee_pic : null
         * module_name : 任务
         * join_status : 0
         * customArr : {"personnel_create_by":"2","reference_relation":"","multitext_desc":"<p>套二厅二套日<\/p><p><br/><\/p>","del_status":"0","datetime_deadline":1530201600000,"datetime_modify_time":1529890115787,"picklist_tag_v":"","datetime_create_time":1529639516905,"personnel_execution":[{"post_name":"员工","name":"张琴","id":2,"picture":"null"}],"personnel_modify_by":"2","text_name":"111","attachment_1520248017179":[],"seas_pool_id":0,"picklist_tag":"5,8,14,12,3","beanName":"project_custom_51","id":1,"beanId":1}
         * label_id :
         * executor_id : 2
         */

        private String task_name;
        private String bean_type;
        private String description;
        private String del_status;
        private String quote_status;
        private String copy_task_id;
        private int create_by;
        private String bean_name;
        private int complete_number;
        private int project_id;
        private String copy_status;
        private int id;
        private String task_status;
        private String finished_time;
        private long create_time;
        private int sub_id;
        private String personnel_collaboratives;
        private long end_time;
        private String complete_status;
        private String employee_name;
        private int sort;
        private String check_member;
        private String check_status;
        private String passed_status;
        private int relation_id;
        private String module_id;
        private String quote_task_id;
        //协作人是否可见
        private String associates_status;
        private String job_id;
        private String employee_pic;
        private String module_name;
        private String join_status;
        private Map<String, Object> customArr;
        private String label_id;
        private int executor_id;

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getBean_type() {
            return bean_type;
        }

        public void setBean_type(String bean_type) {
            this.bean_type = bean_type;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getQuote_status() {
            return quote_status;
        }

        public void setQuote_status(String quote_status) {
            this.quote_status = quote_status;
        }

        public String getCopy_task_id() {
            return copy_task_id;
        }

        public String getAssociates_status() {
            return associates_status;
        }

        public void setAssociates_status(String associates_status) {
            this.associates_status = associates_status;
        }

        public void setCopy_task_id(String copy_task_id) {
            this.copy_task_id = copy_task_id;
        }

        public int getCreate_by() {
            return create_by;
        }

        public void setCreate_by(int create_by) {
            this.create_by = create_by;
        }

        public String getBean_name() {
            return bean_name;
        }

        public void setBean_name(String bean_name) {
            this.bean_name = bean_name;
        }

        public int getComplete_number() {
            return complete_number;
        }

        public void setComplete_number(int complete_number) {
            this.complete_number = complete_number;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getCopy_status() {
            return copy_status;
        }

        public void setCopy_status(String copy_status) {
            this.copy_status = copy_status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTask_status() {
            return task_status;
        }

        public void setTask_status(String task_status) {
            this.task_status = task_status;
        }

        public String getFinished_time() {
            return finished_time;
        }

        public void setFinished_time(String finished_time) {
            this.finished_time = finished_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getSub_id() {
            return sub_id;
        }

        public void setSub_id(int sub_id) {
            this.sub_id = sub_id;
        }


        public String getPersonnel_collaboratives() {
            return personnel_collaboratives;
        }

        public void setPersonnel_collaboratives(String personnel_collaboratives) {
            this.personnel_collaboratives = personnel_collaboratives;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public String getComplete_status() {
            return complete_status;
        }

        public void setComplete_status(String complete_status) {
            this.complete_status = complete_status;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
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

        public String getPassed_status() {
            return passed_status;
        }

        public void setPassed_status(String passed_status) {
            this.passed_status = passed_status;
        }

        public int getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(int relation_id) {
            this.relation_id = relation_id;
        }

        public String getModule_id() {
            return module_id;
        }

        public void setModule_id(String module_id) {
            this.module_id = module_id;
        }

        public String getQuote_task_id() {
            return quote_task_id;
        }

        public void setQuote_task_id(String quote_task_id) {
            this.quote_task_id = quote_task_id;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getEmployee_pic() {
            return employee_pic;
        }

        public void setEmployee_pic(String employee_pic) {
            this.employee_pic = employee_pic;
        }

        public String getModule_name() {
            return module_name;
        }

        public void setModule_name(String module_name) {
            this.module_name = module_name;
        }

        public String getJoin_status() {
            return join_status;
        }

        public void setJoin_status(String join_status) {
            this.join_status = join_status;
        }

        public Map<String, Object> getCustomArr() {
            return customArr;
        }

        public void setCustomArr(Map<String, Object> customArr) {
            this.customArr = customArr;
        }

        public String getLabel_id() {
            return label_id;
        }

        public void setLabel_id(String label_id) {
            this.label_id = label_id;
        }

        public int getExecutor_id() {
            return executor_id;
        }

        public void setExecutor_id(int executor_id) {
            this.executor_id = executor_id;
        }


    }
}
