package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */

public class TaskRepeatResultBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private long id;
        private long task_id;
        private int repeat_type;
        //频率
        private String repeat_unit;
        //每周重复星期 , 每月重复日期
        private String frequency_unit;
        //结束重复方式 0:永不 1:次数 2:日期
        private int end_way;
        //结束次数
        private int end_of_times;
        //结束日期
        private String end_time;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTask_id() {
            return task_id;
        }

        public void setTask_id(long task_id) {
            this.task_id = task_id;
        }

        public int getRepeat_type() {
            return repeat_type;
        }

        public void setRepeat_type(int repeat_type) {
            this.repeat_type = repeat_type;
        }

        public String getRepeat_unit() {
            return repeat_unit;
        }

        public void setRepeat_unit(String repeat_unit) {
            this.repeat_unit = repeat_unit;
        }

        public String getFrequency_unit() {
            return frequency_unit;
        }

        public void setFrequency_unit(String frequency_unit) {
            this.frequency_unit = frequency_unit;
        }

        public int getEnd_way() {
            return end_way;
        }

        public void setEnd_way(int end_way) {
            this.end_way = end_way;
        }

        public int getEnd_of_times() {
            return end_of_times;
        }

        public void setEnd_of_times(int end_of_times) {
            this.end_of_times = end_of_times;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
