package com.hjhq.teamface.memo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 * Describe：
 */

public class MemoContentBean implements Serializable {

    /**
     * num : 4
     * check : 1
     * type : 1
     * content : 宝贝宝贝
     */

    private int num;
    private int check;
    private int type;
    private String content;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
