package com.hjhq.teamface.basis.bean;


import com.hjhq.teamface.basis.R;

/**
 * Created by lx on 2017/8/31.
 */

public class ToolMenu {
    private int id;
    private String title;
    private Integer icon;
    private int titleColor = R.color.title_bar_right_txt_default_color;
    private int titleSize = R.dimen.title_bar_right_txt_size;
    private boolean isShow = true;

    public ToolMenu() {
    }

    public ToolMenu(int id, String title, Integer icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public ToolMenu(int id, String title, int color, Integer icon) {
        this.id = id;
        this.icon = icon;
        this.title = title == null?"":title;
        if (color != 0)
            this.titleColor = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
