package com.hjhq.teamface.basis.bean;

/**
 * Created by Administrator on 2017/9/22.
 * Describe：
 */

public class ImMessage {
    int code;
    String tag;
    Object o;


    public ImMessage(int code, String tag, Object o) {
        this.code = code;
        this.tag = tag;
        this.o = o;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return o;
    }

    public void setObject(Object o) {
        this.o = o;
    }
}
