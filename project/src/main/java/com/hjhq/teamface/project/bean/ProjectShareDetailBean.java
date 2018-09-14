package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/23.
 * Describe：项目分享详情数据类
 */

public class ProjectShareDetailBean extends BaseBean implements Serializable {


    /**
     * data : {"share_ids":"1,2","share_relevance_arr":"","create_time":1524711911454,"share_status":"1","modify_time":"","share_top_status":"0","del_status":"0","modify_by":"","submit_status":"1","share_content":"项目分享内容","create_by":1,"share_praise_status":0,"share_title":"王克栋项目分享-0426-1","project_id":9,"share_top_time":"","id":2}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * share_ids : 1,2
         * share_relevance_arr :
         * create_time : 1524711911454
         * share_status : 1
         * modify_time :
         * share_top_status : 0
         * del_status : 0
         * modify_by :
         * submit_status : 1
         * share_content : 项目分享内容
         * create_by : 1
         * share_praise_status : 0
         * share_title : 王克栋项目分享-0426-1
         * project_id : 9
         * share_top_time :
         * id : 2
         */

        private String share_ids;
        private String share_relevance_arr;
        private String create_time;
        private String share_status;
        private String modify_time;
        private String share_top_status;
        private String del_status;
        private String modify_by;
        private String submit_status;
        private String share_content;
        private String create_by;
        private String share_praise_status;
        private String share_title;
        private String project_id;
        private String share_top_time;
        private String id;
        private String share_praise_number;
        private ArrayList<ProjectMemberBean> praise_obj;
        private ArrayList<ProjectMemberBean> share_obj;

        public String getShare_praise_number() {
            return share_praise_number;
        }

        public void setShare_praise_number(String share_praise_number) {
            this.share_praise_number = share_praise_number;
        }

        public ArrayList<ProjectMemberBean> getPraiseObj() {
            return praise_obj;
        }

        public void setPraiseObj(ArrayList<ProjectMemberBean> praiseObj) {
            this.praise_obj = praiseObj;
        }

        public ArrayList<ProjectMemberBean> getShareObj() {
            return share_obj;
        }

        public void setShareObj(ArrayList<ProjectMemberBean> shareObj) {
            this.share_obj = shareObj;
        }

        public String getShare_ids() {
            return share_ids;
        }

        public void setShare_ids(String share_ids) {
            this.share_ids = share_ids;
        }

        public String getShare_relevance_arr() {
            return share_relevance_arr;
        }

        public void setShare_relevance_arr(String share_relevance_arr) {
            this.share_relevance_arr = share_relevance_arr;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getShare_status() {
            return share_status;
        }

        public void setShare_status(String share_status) {
            this.share_status = share_status;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getShare_top_status() {
            return share_top_status;
        }

        public void setShare_top_status(String share_top_status) {
            this.share_top_status = share_top_status;
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

        public String getSubmit_status() {
            return submit_status;
        }

        public void setSubmit_status(String submit_status) {
            this.submit_status = submit_status;
        }

        public String getShare_content() {
            return share_content;
        }

        public void setShare_content(String share_content) {
            this.share_content = share_content;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public String getShare_praise_status() {
            return share_praise_status;
        }

        public void setShare_praise_status(String share_praise_status) {
            this.share_praise_status = share_praise_status;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getShare_top_time() {
            return share_top_time;
        }

        public void setShare_top_time(String share_top_time) {
            this.share_top_time = share_top_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
