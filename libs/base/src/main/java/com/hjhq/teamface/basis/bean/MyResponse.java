package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 *
 * @author xj
 * @date 2017/3/15
 */

public class MyResponse implements Serializable{

    /**
     * code : 0
     * describe : 
     */

    private int code;
    private String describe;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
