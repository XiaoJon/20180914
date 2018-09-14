package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/8/9.
 */

public class TaskRepeatRequestBean {
    private Long id;
    private long task_id;
    //重复  0:天 1:周2:月 3:从不重复
    private int repeat_type;
    //频率
    private String repeat_unit;
    //每周重复星期 , 每月重复日期
    private String frequency_unit;
    //结束重复方式 0:永不 1:次数 2:日期
    private Integer end_way;
    //结束次数
    private Integer end_of_times;
    //结束日期
    private Long end_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getEnd_way() {
        return end_way;
    }

    public void setEnd_way(Integer end_way) {
        this.end_way = end_way;
    }

    public Integer getEnd_of_times() {
        return end_of_times;
    }

    public void setEnd_of_times(Integer end_of_times) {
        this.end_of_times = end_of_times;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
}
