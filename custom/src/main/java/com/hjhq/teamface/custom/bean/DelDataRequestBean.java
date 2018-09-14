package com.hjhq.teamface.custom.bean;

/**
 * 删除数据请求接口
 * Created by lx on 2017/9/25.
 */

public class DelDataRequestBean {
    public DelDataRequestBean(){}
    public DelDataRequestBean(String[] ids, String bean){
        this.ids = ids;
        this.bean = bean;
    }
    public DelDataRequestBean(String id, String bean){
        this.ids = new String[]{id};
        this.bean = bean;
    }

    private String[] ids;

    private String bean;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }
}
