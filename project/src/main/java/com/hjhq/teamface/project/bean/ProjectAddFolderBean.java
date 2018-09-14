package com.hjhq.teamface.project.bean;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */

public class ProjectAddFolderBean {
    private String name;
    private long parnent_id;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParnent_id() {
        return parnent_id;
    }

    public void setParnent_id(long parnent_id) {
        this.parnent_id = parnent_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
