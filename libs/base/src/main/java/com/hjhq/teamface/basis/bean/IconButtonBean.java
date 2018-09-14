package com.hjhq.teamface.basis.bean;

/**
 * Created by lx on 2017/7/5.
 */

public class IconButtonBean {
    private String title;
    private int icon;

    public IconButtonBean(){}
    public IconButtonBean(String title,int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
