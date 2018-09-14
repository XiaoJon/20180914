package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */

public class PersonalTaskRemindResultBean extends BaseBean {

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
        private int remind_type;
        private String remind_content;
        private long task_id;
        private int remind_unit;
        private String before_deadline;
        private String remind_way;
        private String remind_time;
        private long id;
        private List<Member> reminder;

        public String getFrom_type() {
            return from_type;
        }

        public void setFrom_type(String from_type) {
            this.from_type = from_type;
        }

        public int getRemind_type() {
            return remind_type;
        }

        public void setRemind_type(int remind_type) {
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

        public int getRemind_unit() {
            return remind_unit;
        }

        public void setRemind_unit(int remind_unit) {
            this.remind_unit = remind_unit;
        }

        public String getBefore_deadline() {
            return before_deadline;
        }

        public void setBefore_deadline(String before_deadline) {
            this.before_deadline = before_deadline;
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

        public String getRemind_way() {
            return remind_way;
        }

        public void setRemind_way(String remind_way) {
            this.remind_way = remind_way;
        }

        public List<Member> getReminder() {
            return reminder;
        }

        public void setReminder(List<Member> reminder) {
            this.reminder = reminder;
        }
    }
}
