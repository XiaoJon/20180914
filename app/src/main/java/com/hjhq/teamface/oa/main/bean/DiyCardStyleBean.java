package com.hjhq.teamface.oa.main.bean;

/**
 * Created by Administrator on 2018/7/13.
 * Describe： 自定义名片模板样式数据类
 */

public class DiyCardStyleBean {
    int type;
    String name;
    boolean checked;
    int res;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
