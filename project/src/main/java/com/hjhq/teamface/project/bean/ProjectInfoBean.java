package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2018/4/23.
 * Describe：项目设置详情信息
 */

public class ProjectInfoBean extends BaseBean {
    /**
     * data : {"note":"项目描述","leader":3,"star_level":0,"temp_status":"0","temp_id":1,"modify_time":"","project_status":"0","visual_range_status":"0","del_status":"0","modify_by":"","project_remind_type":"0","project_progress_status":"0","create_by":3,"project_remind_title":"0","id":3,"project_remind_unit":"0","leader_pic":"","create_time":1525229964559,"leader_name":"陈宇亮","star_time":0,"end_time":1524539367,"sort":0,"temp_type":"","project_progress_content":"","project_remind_content":"","name":"王克栋项目0502-2","pic_url":""}
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
         * note : 项目描述
         * leader : 3
         * star_level : 0
         * temp_status : 0
         * temp_id : 1
         * modify_time :
         * project_status : 0
         * visual_range_status : 0
         * del_status : 0
         * modify_by :
         * project_remind_type : 0
         * project_progress_status : 0
         * create_by : 3
         * project_remind_title : 0
         * id : 3
         * project_remind_unit : 0
         * leader_pic :
         * create_time : 1525229964559
         * leader_name : 陈宇亮
         * star_time : 0
         * end_time : 1524539367
         * sort : 0
         * temp_type :
         * project_progress_content :
         * project_remind_content :
         * name : 王克栋项目0502-2
         * pic_url :
         */

        private String note;
        private String leader;
        private String star_level;
        private String temp_status;
        private String temp_id;
        private String modify_time;
        //项目状态（0进行中（启用） 1归档 2暂停 3删除 ）
        private String project_status;
        //项目可见范围（0不公开 1公开）
        private String visual_range_status;
        private String del_status;
        private String modify_by;
        private String project_remind_type;
        //0自动计算 2手动填写
        private String project_progress_status;
        private String create_by;
        private String project_remind_title;
        private String id;
        private String project_remind_unit;
        private String leader_pic;
        private String create_time;
        private String leader_name;
        private String star_time;
        private String start_time;
        private String end_time;
        private String sort;
        private String temp_type;
        //手动填写内容（正整数）
        private String project_progress_content;
        private String project_remind_content;
        private String name;
        private String pic_url;
        private String system_default_pic;
        private String project_labels_content;
        private String project_time_status;
        //项目进度百分比，project_progress_status 为0的时候百分百进度用这个显示
        private String project_progress_number;
        private String deadline_status;
        private String project_complete_status;


        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getSystem_default_pic() {
            return system_default_pic;
        }

        public void setSystem_default_pic(String system_default_pic) {
            this.system_default_pic = system_default_pic;
        }

        public String getProject_labels_content() {
            return project_labels_content;
        }

        public void setProject_labels_content(String project_labels_content) {
            this.project_labels_content = project_labels_content;
        }

        public String getProject_time_status() {
            return project_time_status;
        }

        public void setProject_time_status(String project_time_status) {
            this.project_time_status = project_time_status;
        }

        public String getProject_progress_number() {
            return project_progress_number;
        }

        public void setProject_progress_number(String project_progress_number) {
            this.project_progress_number = project_progress_number;
        }

        public String getDeadline_status() {
            return deadline_status;
        }

        public void setDeadline_status(String deadline_status) {
            this.deadline_status = deadline_status;
        }

        public String getProject_complete_status() {
            return project_complete_status;
        }

        public void setProject_complete_status(String project_complete_status) {
            this.project_complete_status = project_complete_status;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getStar_level() {
            return star_level;
        }

        public void setStar_level(String star_level) {
            this.star_level = star_level;
        }

        public String getTemp_status() {
            return temp_status;
        }

        public void setTemp_status(String temp_status) {
            this.temp_status = temp_status;
        }

        public String getTemp_id() {
            return temp_id;
        }

        public void setTemp_id(String temp_id) {
            this.temp_id = temp_id;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getProject_status() {
            return project_status;
        }

        public void setProject_status(String project_status) {
            this.project_status = project_status;
        }

        public String getVisual_range_status() {
            return visual_range_status;
        }

        public void setVisual_range_status(String visual_range_status) {
            this.visual_range_status = visual_range_status;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getModify_by() {
            return modify_by;
        }

        public void setModify_by(String modify_by) {
            this.modify_by = modify_by;
        }

        public String getProject_remind_type() {
            return project_remind_type;
        }

        public void setProject_remind_type(String project_remind_type) {
            this.project_remind_type = project_remind_type;
        }

        public String getProject_progress_status() {
            return project_progress_status;
        }

        public void setProject_progress_status(String project_progress_status) {
            this.project_progress_status = project_progress_status;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getProject_remind_title() {
            return project_remind_title;
        }

        public void setProject_remind_title(String project_remind_title) {
            this.project_remind_title = project_remind_title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProject_remind_unit() {
            return project_remind_unit;
        }

        public void setProject_remind_unit(String project_remind_unit) {
            this.project_remind_unit = project_remind_unit;
        }

        public String getLeader_pic() {
            return leader_pic;
        }

        public void setLeader_pic(String leader_pic) {
            this.leader_pic = leader_pic;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLeader_name() {
            return leader_name;
        }

        public void setLeader_name(String leader_name) {
            this.leader_name = leader_name;
        }

        public String getStar_time() {
            return star_time;
        }

        public void setStar_time(String star_time) {
            this.star_time = star_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getTemp_type() {
            return temp_type;
        }

        public void setTemp_type(String temp_type) {
            this.temp_type = temp_type;
        }

        public String getProject_progress_content() {
            return project_progress_content;
        }

        public void setProject_progress_content(String project_progress_content) {
            this.project_progress_content = project_progress_content;
        }

        public String getProject_remind_content() {
            return project_remind_content;
        }

        public void setProject_remind_content(String project_remind_content) {
            this.project_remind_content = project_remind_content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }
    }


//    {
//        "data": {
//        " id ":1,                           //记录ID
//                "project_id ":2,                    //项目ID
//                "name":项目协作,                    //项目名称
//                "leader":2,                         //项目负责人
//                "project_progress_status":0         //0自动计算 2手动填写
//        "project_progress_content": 50      //手动填写内容（正整数）
//        "start_time ": 1111                  //项目开始时间（时间戳）
//        "end_time ": 1111                    //项目截止时间（时间戳）
//        "visual_range ": 0                   //项目可见范围（0不公开 1公开）
//        "describe ": 管理系统研发           //项目描述
//        "project_remind_title": 系统消息提示//提示主题（预留）
//        "project_remind_content": 1-60      //项目提醒内容
//        "project_remind_unit": 分钟         //项目提醒单位
//        "project_remind_type": 0            //提醒方式（0企信 1企业微信 2钉钉 3邮件）
//        "project_status ": 0                //项目状态（0进行中（启用） 1归档 2暂停 3删除 ）
//    }
//
//        response":{
//        "code":1001,
//            "describe":"执行成功"
//    }


}
