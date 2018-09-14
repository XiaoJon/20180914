package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 动态列表
 * Created by lx on 2017/9/25.
 */

public class DynamicListResultBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timeList : [{"user_name":"caojianhua","id":"5","time":"1506050565812","content":"新增null","bean":"customer","relation_id":"123456"},{"user_name":"caojianhua","id":"6","time":"1506051539690","content":"新增null","bean":"customer","relation_id":"123456"},{"user_name":"caojianhua","id":"24","time":"1506079996342","content":"新增null","bean":"customer","relation_id":"123456"},{"user_name":"caojianhua","id":"15","time":"1506078070755","content":"新增null","bean":"customer","relation_id":"123456"}]
         * timeDate : 1506009600000
         */

        private String timeDate;
        private List<TimeListBean> timeList;

        public String getTimeDate() {
            return timeDate;
        }

        public void setTimeDate(String timeDate) {
            this.timeDate = timeDate;
        }

        public List<TimeListBean> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<TimeListBean> timeList) {
            this.timeList = timeList;
        }

        public static class TimeListBean {
            /**
             * user_name : caojianhua
             * id : 5
             * time : 1506050565812
             * content : 新增null
             * bean : customer
             * relation_id : 123456
             */

            private String employee_name;
            private String id;
            private String datetime_time;
            private String content;
            private String bean;
            private String relation_id;
            private String timeDate;


            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDatetime_time() {
                return datetime_time;
            }

            public void setDatetime_time(String datetime_time) {
                this.datetime_time = datetime_time;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getBean() {
                return bean;
            }

            public void setBean(String bean) {
                this.bean = bean;
            }

            public String getRelation_id() {
                return relation_id;
            }

            public void setRelation_id(String relation_id) {
                this.relation_id = relation_id;
            }

            public String getTimeDate() {
                return timeDate;
            }

            public void setTimeDate(String timeDate) {
                this.timeDate = timeDate;
            }
        }
    }
}
