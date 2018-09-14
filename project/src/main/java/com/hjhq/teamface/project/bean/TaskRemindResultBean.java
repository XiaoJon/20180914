package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */

public class TaskRemindResultBean extends BaseBean {

    /**
     * data : [{"from_type":"0","reminder":[{"employee_id":19,"employee_pic":"","employee_name":"YLG"}],"create_by":19,"remind_type":"1","remind_content":"","task_id":114,"del_status":"0","remind_unit":"","before_deadline":"","remind_way":"0","modify_time":1532319481304,"create_time":1532319481304,"remind_time":1532319459000,"modify_by":19,"id":43}]
     * response : {"code":1001,"describe":"执行成功"}
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * from_type : 0
         * reminder : [{"employee_id":19,"employee_pic":"","employee_name":"YLG"}]
         * create_by : 19
         * remind_type : 1
         * remind_content :
         * task_id : 114
         * del_status : 0
         * remind_unit :
         * before_deadline :
         * remind_way : 0
         * modify_time : 1532319481304
         * create_time : 1532319481304
         * remind_time : 1532319459000
         * modify_by : 19
         * id : 43
         */

        private String from_type;
        private String remind_type;
        private String remind_content;
        private long task_id;
        private String remind_unit;
        private String before_deadline;
        private String remind_way;
        private String remind_time;
        private long id;
        private List<ReminderBean> reminder;

        public String getFrom_type() {
            return from_type;
        }

        public void setFrom_type(String from_type) {
            this.from_type = from_type;
        }

        public String getRemind_type() {
            return remind_type;
        }

        public void setRemind_type(String remind_type) {
            this.remind_type = remind_type;
        }

        public String getRemind_content() {
            return remind_content;
        }

        public void setRemind_content(String remind_content) {
            this.remind_content = remind_content;
        }

        public long getTask_id() {
            return task_id;
        }

        public void setTask_id(long task_id) {
            this.task_id = task_id;
        }

        public String getRemind_unit() {
            return remind_unit;
        }

        public void setRemind_unit(String remind_unit) {
            this.remind_unit = remind_unit;
        }

        public String getBefore_deadline() {
            return before_deadline;
        }

        public void setBefore_deadline(String before_deadline) {
            this.before_deadline = before_deadline;
        }

        public String getRemind_way() {
            return remind_way;
        }

        public void setRemind_way(String remind_way) {
            this.remind_way = remind_way;
        }

        public String getRemind_time() {
            return remind_time;
        }

        public void setRemind_time(String remind_time) {
            this.remind_time = remind_time;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<ReminderBean> getReminder() {
            return reminder;
        }

        public void setReminder(List<ReminderBean> reminder) {
            this.reminder = reminder;
        }

        public static class ReminderBean {
            /**
             * employee_id : 19
             * employee_pic :
             * employee_name : YLG
             */

            private long employee_id;
            private String employee_pic;
            private String employee_name;

            public long getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(long employee_id) {
                this.employee_id = employee_id;
            }

            public String getEmployee_pic() {
                return employee_pic;
            }

            public void setEmployee_pic(String employee_pic) {
                this.employee_pic = employee_pic;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }
        }
    }
}
