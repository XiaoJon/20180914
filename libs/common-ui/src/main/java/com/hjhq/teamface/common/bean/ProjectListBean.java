package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/4/24
 */

public class ProjectListBean extends BaseBean {

    /**
     * data : {"dataList":[{"note":"1111111","leader":1,"star_level":"","temp_status":"0","create_time":1524540242454,"temp_id":0,"star_time":"","modify_time":"","end_time":1524540213654,"visual_range_status":"1","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"jun","id":3,"pic_url":""},{"note":"好好用哦","leader":2,"star_level":"","temp_status":"0","create_time":1524540118876,"temp_id":0,"star_time":"","modify_time":"","end_time":1524799260000,"visual_range_status":"0","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"好吧、在于","id":2,"pic_url":""},{"note":"项目描述","leader":1,"star_level":"","temp_status":"0","create_time":1524539932766,"temp_id":1,"star_time":"","modify_time":"","end_time":1524539367,"visual_range_status":"0","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"王克栋项目1","id":1,"pic_url":""}],"pageInfo":{"totalPages":1,"pageSize":20,"totalRows":3,"pageNum":1,"curPageSize":3}}
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
         * dataList : [{"note":"1111111","leader":1,"star_level":"","temp_status":"0","create_time":1524540242454,"temp_id":0,"star_time":"","modify_time":"","end_time":1524540213654,"visual_range_status":"1","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"jun","id":3,"pic_url":""},{"note":"好好用哦","leader":2,"star_level":"","temp_status":"0","create_time":1524540118876,"temp_id":0,"star_time":"","modify_time":"","end_time":1524799260000,"visual_range_status":"0","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"好吧、在于","id":2,"pic_url":""},{"note":"项目描述","leader":1,"star_level":"","temp_status":"0","create_time":1524539932766,"temp_id":1,"star_time":"","modify_time":"","end_time":1524539367,"visual_range_status":"0","del_status":"0","sort":"","modify_by":"","create_by":1,"temp_type":"","name":"王克栋项目1","id":1,"pic_url":""}]
         * pageInfo : {"totalPages":1,"pageSize":20,"totalRows":3,"pageNum":1,"curPageSize":3}
         */

        private PageInfo pageInfo;
        private List<DataListBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }


        public static class DataListBean implements Serializable {
            /**
             * note : 1111111
             * leader : 1
             * star_level :
             * temp_status : 0
             * create_time : 1524540242454
             * temp_id : 0
             * star_time :
             * modify_time :
             * end_time : 1524540213654
             * visual_range_status : 1
             * del_status : 0
             * sort :
             * modify_by :
             * create_by : 1
             * temp_type :
             * name : jun
             * id : 3
             * pic_url :
             */

            private String note;
            private String leader;
            private String star_level;
            private String temp_status;
            private int temp_id;
            private String visual_range_status;
            private String sort;
            private String temp_type;
            private String project_status;
            private String name;
            private long id;
            private String pic_url;
            private String deadline_status;//期限状态 0，1超期
            private String project_progress_content;//手动输入进度
            private String project_progress_status; //项目进展选择状态0自动计算 1手动填写
            private String task_complete_count;//任务完成数据
            private String task_count;

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

            public String getProject_status() {
                return project_status;
            }

            public void setProject_status(String project_status) {
                this.project_status = project_status;
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

            public int getTemp_id() {
                return temp_id;
            }

            public void setTemp_id(int temp_id) {
                this.temp_id = temp_id;
            }

            public String getVisual_range_status() {
                return visual_range_status;
            }

            public void setVisual_range_status(String visual_range_status) {
                this.visual_range_status = visual_range_status;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }

            public String getDeadline_status() {
                return deadline_status;
            }

            public void setDeadline_status(String deadline_status) {
                this.deadline_status = deadline_status;
            }

            public String getProject_progress_content() {
                return project_progress_content;
            }

            public void setProject_progress_content(String project_progress_content) {
                this.project_progress_content = project_progress_content;
            }

            public String getProject_progress_status() {
                return project_progress_status;
            }

            public void setProject_progress_status(String project_progress_status) {
                this.project_progress_status = project_progress_status;
            }

            public String getTask_complete_count() {
                return task_complete_count;
            }

            public void setTask_complete_count(String task_complete_count) {
                this.task_complete_count = task_complete_count;
            }

            public String getTask_count() {
                return task_count;
            }

            public void setTask_count(String task_count) {
                this.task_count = task_count;
            }
        }
    }
}
