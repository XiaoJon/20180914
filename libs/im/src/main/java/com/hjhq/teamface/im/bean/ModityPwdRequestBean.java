package com.hjhq.teamface.im.bean;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ModityPwdRequestBean {


    public ModityPwdRequestBean(String userCode, String passWord, String code) {
        this.userCode = userCode;
        this.passWord = passWord;
        this.code = code;
    }

    /**
     * userCode : 15712113669
     * passWord : 1234567
     * code : 888888
     */

    private String userCode;
    private String passWord;
    private String code;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
