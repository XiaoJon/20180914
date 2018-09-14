package com.hjhq.teamface.project.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/26.
 */

public class TaskLikeBean implements Serializable {

    /**
     * share_id : 349
     * create_by : 2
     * create_time : 1529890500716
     * modify_time :
     * employee_pic : http://192.168.1.58:8281/custom-gateway/common/file/imageDownload?bean=company&fileName=201806251810360.jpg&fileSize=36791
     * del_status : 0
     * employee_name : 张琴
     * id : 26
     * modify_by :
     * type_status : 1
     */

    private String share_id;
    private String create_by;
    private String create_time;
    private String modify_time;
    private String picture;
    private String del_status;
    private String name;
    private String id;
    private String modify_by;
    private String type_status;

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModify_by() {
        return modify_by;
    }

    public void setModify_by(String modify_by) {
        this.modify_by = modify_by;
    }

    public String getType_status() {
        return type_status;
    }

    public void setType_status(String type_status) {
        this.type_status = type_status;
    }
}
