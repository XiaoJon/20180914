package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/8/9.
 */

public class TaskRemindRequestBean {
    private Long id;
    private long task_id;
    //1，任务关联信息 2，子任务关联信息
    private long from_type;
    //提醒方式  1:自定义提醒 2:截止时间提醒
    private long remind_type;
    //提醒时间
    private long remind_time;
    //提醒内容
    private String remind_content;
    //时间单位 0分 1小时 2天
    private long remind_unit;
    //截止时间数字
    private String before_deadline;
    //提醒人用户编号，多个使用英文的逗号隔开
    private String reminder;
    //提醒方式0企信 1微信 2钉钉 3邮件
    private int remind_way;
    private Long project_id;

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

    public long getFrom_type() {
        return from_type;
    }

    public void setFrom_type(long from_type) {
        this.from_type = from_type;
    }

    public long getRemind_type() {
        return remind_type;
    }

    public void setRemind_type(long remind_type) {
        this.remind_type = remind_type;
    }

    public long getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(long remind_time) {
        this.remind_time = remind_time;
    }

    public String getRemind_content() {
        return remind_content;
    }

    public void setRemind_content(String remind_content) {
        this.remind_content = remind_content;
    }

    public long getRemind_unit() {
        return remind_unit;
    }

    public void setRemind_unit(long remind_unit) {
        this.remind_unit = remind_unit;
    }

    public String getBefore_deadline() {
        return before_deadline;
    }

    public void setBefore_deadline(String before_deadline) {
        this.before_deadline = before_deadline;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public int getRemind_way() {
        return remind_way;
    }

    public void setRemind_way(int remind_way) {
        this.remind_way = remind_way;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }
}
