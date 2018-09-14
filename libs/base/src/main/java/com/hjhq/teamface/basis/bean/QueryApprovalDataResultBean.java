package com.hjhq.teamface.basis.bean;

/**
 * 查询审批数据 实体
 * Created by Administrator on 2018/4/28.
 */

public class QueryApprovalDataResultBean extends BaseBean {


    /**
     * data : {"module_bean":"bean1524898371070","module_data_id":"","create_time":1524898914338,"approval_data_id":4,"task_id":"167613","del_status":0,"begin_user_id":1,"picture":"http://192.168.1.188:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=2/company/1524103301364.20160228202232_vFaTz.jpeg","process_definition_id":"167604","process_field_v":1524898397438,"process_name":"随便模块","process_status":0,"task_key":"firstTask","process_key":"process1524898396844","process_v":186,"begin_user_name":"彭华娣","id":286}
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
         * module_bean : bean1524898371070
         * module_data_id :
         * create_time : 1524898914338
         * approval_data_id : 4
         * task_id : 167613
         * del_status : 0
         * begin_user_id : 1
         * picture : http://192.168.1.188:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=2/company/1524103301364.20160228202232_vFaTz.jpeg
         * process_definition_id : 167604
         * process_field_v : 1524898397438
         * process_name : 随便模块
         * process_status : 0
         * task_key : firstTask
         * process_key : process1524898396844
         * process_v : 186
         * begin_user_name : 彭华娣
         * id : 286
         */

        private String module_bean;
        private String module_data_id;
        private String create_time;
        private String approval_data_id;
        private String task_id;
        private String del_status;
        private String begin_user_id;
        private String picture;
        private String process_definition_id;
        private String process_field_v;
        private String process_name;
        private String process_status;
        private String task_key;
        private String task_name;
        private String process_key;
        private String process_v;
        private String fromType;
        private String begin_user_name;
        private String id;

        public String getModule_bean() {
            return module_bean;
        }

        public void setModule_bean(String module_bean) {
            this.module_bean = module_bean;
        }

        public String getModule_data_id() {
            return module_data_id;
        }

        public void setModule_data_id(String module_data_id) {
            this.module_data_id = module_data_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getApproval_data_id() {
            return approval_data_id;
        }

        public void setApproval_data_id(String approval_data_id) {
            this.approval_data_id = approval_data_id;
        }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getBegin_user_id() {
            return begin_user_id;
        }

        public void setBegin_user_id(String begin_user_id) {
            this.begin_user_id = begin_user_id;
        }

        public String getFromType() {
            return fromType;
        }

        public void setFromType(String fromType) {
            this.fromType = fromType;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getProcess_definition_id() {
            return process_definition_id;
        }

        public void setProcess_definition_id(String process_definition_id) {
            this.process_definition_id = process_definition_id;
        }

        public String getProcess_field_v() {
            return process_field_v;
        }

        public void setProcess_field_v(String process_field_v) {
            this.process_field_v = process_field_v;
        }

        public String getProcess_name() {
            return process_name;
        }

        public void setProcess_name(String process_name) {
            this.process_name = process_name;
        }

        public String getProcess_status() {
            return process_status;
        }

        public void setProcess_status(String process_status) {
            this.process_status = process_status;
        }

        public String getTask_key() {
            return task_key;
        }

        public void setTask_key(String task_key) {
            this.task_key = task_key;
        }

        public String getProcess_key() {
            return process_key;
        }

        public void setProcess_key(String process_key) {
            this.process_key = process_key;
        }

        public String getProcess_v() {
            return process_v;
        }

        public void setProcess_v(String process_v) {
            this.process_v = process_v;
        }

        public String getBegin_user_name() {
            return begin_user_name;
        }

        public void setBegin_user_name(String begin_user_name) {
            this.begin_user_name = begin_user_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
