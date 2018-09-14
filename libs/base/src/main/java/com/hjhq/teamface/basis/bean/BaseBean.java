package com.hjhq.teamface.basis.bean;

/**
 * 由于牵扯太多，未将 data 对象改成泛型
 * Created by lx on 2017/3/15.
 */

public class BaseBean {

    MyResponse response;

    public MyResponse getResponse() {

        return response;
    }

    public void setResponse(MyResponse response) {
        this.response = response;
    }
}
