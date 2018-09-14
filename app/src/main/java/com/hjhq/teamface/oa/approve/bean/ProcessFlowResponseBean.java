package com.hjhq.teamface.oa.approve.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 审批流程
 * Created by Administrator on 2018/1/17.
 */

public class ProcessFlowResponseBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * task_name : 开始任务
         * approval_employee_picture :
         * next_task_key : task1516161174511
         * approval_message : 提交审批
         * task_id : 215014
         * task_status_id : -1
         * approval_employee_name : 曹建华
         * process_definition_id : 215005
         * next_task_name : 新节点
         * task_status_name : 已提交
         * next_approval_employee_id : 8
         * task_key : firstTask
         * approval_time : 1516161276653
         * approval_employee_post : 产品总监
         * id : 201
         * approval_employee_id : 1
         */

        private String task_name;
        private String approval_employee_picture;
        private String next_task_key;
        private String approval_message;
        private String task_id;
        private String task_status_id;
        private String normal;//0 异常流程节点（前端只需要处理不显示头像）  1 正常流程节点
        private String approval_employee_name;
        private String process_definition_id;
        private String next_task_name;
        private String task_status_name;
        private String next_approval_employee_id;
        private String task_key;
        private String approval_time;
        private String process_type;//0固定流程  1自由流程
        private String approval_employee_post;
        private String id;
        private String approval_employee_id;
        private String task_approval_type;  // 1会签  2获签 3从角色中指定审批人


        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getApproval_employee_picture() {
            return approval_employee_picture;
        }

        public void setApproval_employee_picture(String approval_employee_picture) {
            this.approval_employee_picture = approval_employee_picture;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }

        public String getNext_task_key() {
            return next_task_key;
        }

        public void setNext_task_key(String next_task_key) {
            this.next_task_key = next_task_key;
        }

        public String getApproval_message() {
            return approval_message;
        }

        public void setApproval_message(String approval_message) {
            this.approval_message = approval_message;
        }

        public String getProcess_type() {
            return process_type;
        }

        public void setProcess_type(String process_type) {
            this.process_type = process_type;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_status_id() {
            return task_status_id;
        }

        public void setTask_status_id(String task_status_id) {
            this.task_status_id = task_status_id;
        }

        public String getApproval_employee_name() {
            return approval_employee_name;
        }

        public void setApproval_employee_name(String approval_employee_name) {
            this.approval_employee_name = approval_employee_name;
        }

        public String getProcess_definition_id() {
            return process_definition_id;
        }

        public void setProcess_definition_id(String process_definition_id) {
            this.process_definition_id = process_definition_id;
        }

        public String getNext_task_name() {
            return next_task_name;
        }

        public void setNext_task_name(String next_task_name) {
            this.next_task_name = next_task_name;
        }

        public String getTask_status_name() {
            return task_status_name;
        }

        public void setTask_status_name(String task_status_name) {
            this.task_status_name = task_status_name;
        }

        public String getNext_approval_employee_id() {
            return next_approval_employee_id;
        }

        public void setNext_approval_employee_id(String next_approval_employee_id) {
            this.next_approval_employee_id = next_approval_employee_id;
        }

        public String getTask_key() {
            return task_key;
        }

        public void setTask_key(String task_key) {
            this.task_key = task_key;
        }

        public String getApproval_time() {
            return approval_time;
        }

        public void setApproval_time(String approval_time) {
            this.approval_time = approval_time;
        }

        public String getApproval_employee_post() {
            return approval_employee_post;
        }

        public void setApproval_employee_post(String approval_employee_post) {
            this.approval_employee_post = approval_employee_post;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApproval_employee_id() {
            return approval_employee_id;
        }

        public void setApproval_employee_id(String approval_employee_id) {
            this.approval_employee_id = approval_employee_id;
        }

        public String getTask_approval_type() {
            return task_approval_type;
        }

        public void setTask_approval_type(String task_approval_type) {
            this.task_approval_type = task_approval_type;
        }
    }
}
