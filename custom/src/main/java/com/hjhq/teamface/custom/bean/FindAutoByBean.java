package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */

public class FindAutoByBean extends BaseBean {

    /**
     * data : {"dataList":[{"chinese_name":"改版","modify_time":1536821406439,"employee_name":"小俊","id":8,"title":"11","english_name":"bean1535075811982"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<DataListBean> dataList;

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            /**
             * chinese_name : 改版
             * modify_time : 1536821406439
             * employee_name : 小俊
             * id : 8
             * title : 11
             * english_name : bean1535075811982
             */

            private String chinese_name;
            private String employee_name;
            private long id;
            private String title;
            private String english_name;

            public String getChinese_name() {
                return chinese_name;
            }

            public void setChinese_name(String chinese_name) {
                this.chinese_name = chinese_name;
            }


            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getEnglish_name() {
                return english_name;
            }

            public void setEnglish_name(String english_name) {
                this.english_name = english_name;
            }
        }
    }
}
