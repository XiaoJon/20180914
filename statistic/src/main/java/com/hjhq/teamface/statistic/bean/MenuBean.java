package com.hjhq.teamface.statistic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13.
 */

public class MenuBean implements Serializable {
    public MenuBean() {
    }

    public MenuBean(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public MenuBean(String id, String name,boolean isCheck) {
        this.id = id;
        this.name = name;
        this.isCheck = isCheck;
    }

    /**
     * id : 0
     * name : 全部市场活动
     */

    private String id;
    private String name;
    private boolean isCheck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
