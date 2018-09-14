package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/20.
 */

public class ApproveListBean implements Serializable {

    /**
     * process_definition_id : 50001
     * module_bean : bean1515549092668
     * module_data_id : 11
     * create_time : 1515579193189
     * process_name : 自由流程请假模块
     * process_status : 0
     * task_key : startEvent
     * process_key : process1515573547973
     * begin_user_name : 肖俊
     * del_status : 0
     * id : 26
     * begin_user_id : 8
     */
    private String process_definition_id;
    //流程字段版本
    private String process_field_v;
    private String module_bean;
    private String approval_data_id;
    private String create_time;
    private String process_name;
    //（0待审批 1审批中 2审批通过 3审批驳回 4已撤销 5流程结束）
    private String process_status;
    private String task_key;
    private String task_name;
    private String task_id;
    private String process_key;
    private String begin_user_name;
    private String del_status;
    private String id;
    private String begin_user_id;
    private String picture;
    private String status; //0 未读 1 已读

    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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

    public String getModule_bean() {
        return module_bean;
    }

    public void setModule_bean(String module_bean) {
        this.module_bean = module_bean;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getApproval_data_id() {
        return approval_data_id;
    }

    public void setApproval_data_id(String approval_data_id) {
        this.approval_data_id = approval_data_id;
    }

    public String getTask_key() {
        return task_key;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
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

    public String getBegin_user_name() {
        return begin_user_name;
    }

    public void setBegin_user_name(String begin_user_name) {
        this.begin_user_name = begin_user_name;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBegin_user_id() {
        return begin_user_id;
    }

    public void setBegin_user_id(String begin_user_id) {
        this.begin_user_id = begin_user_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
