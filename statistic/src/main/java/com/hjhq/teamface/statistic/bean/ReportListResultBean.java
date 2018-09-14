package com.hjhq.teamface.statistic.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 * 报表列表
 * Created by Administrator on 2018/3/19.
 */

public class ReportListResultBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * report_label : 客户-汇总
         * create_time : 1519972994995
         * create_by_id : 1
         * data_source_name : custom
         * id : 1
         * create_by_name : 徐晓鹏
         * data_source_label : 客户
         */

        private List<ListBean> list;

        private PageInfo pageInfo;
        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }
        public static class ListBean {
            /**
             * report_label : 客户-汇总
             * create_time : 1519972994995
             * create_by_id : 1
             * data_source_name : custom
             * id : 1
             * create_by_name : 徐晓鹏
             * data_source_label : 客户
             */

            private String report_label;
            private String create_time;
            private String create_by_id;
            private String data_source_name;
            private String id;
            private String create_by_name;
            private String data_source_label;

            public String getReport_label() {
                return report_label;
            }

            public void setReport_label(String report_label) {
                this.report_label = report_label;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getCreate_by_id() {
                return create_by_id;
            }

            public void setCreate_by_id(String create_by_id) {
                this.create_by_id = create_by_id;
            }

            public String getData_source_name() {
                return data_source_name;
            }

            public void setData_source_name(String data_source_name) {
                this.data_source_name = data_source_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreate_by_name() {
                return create_by_name;
            }

            public void setCreate_by_name(String create_by_name) {
                this.create_by_name = create_by_name;
            }

            public String getData_source_label() {
                return data_source_label;
            }

            public void setData_source_label(String data_source_label) {
                this.data_source_label = data_source_label;
            }
        }


    }
}
