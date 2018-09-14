package com.hjhq.teamface.memo.bean;

/**
 * Created by Administrator on 2018/8/27.
 * Describe：
 */

public class MemberValueBean {

    /**
     * post_name :
     * name : 徐兵
     * id : 38
     * picture : http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534232592741/blob&fileSize=5456
     */

    private String post_name;
    private String name;
    private int id;
    private String picture;

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
