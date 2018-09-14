package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.im.util.ParseUtil;


/**
 * @Title:
 * @Description:登录请求结构体
 * @Author:Administrator
 * @Since:2017年10月11日
 * @Version:1.1.0
 */
public class LoginRequestPojo {
    private String szUsername; // 50

    private byte chStatus; // 1

    private byte chUserType; // 1

    public String getSzUsername() {
        return szUsername;
    }

    public void setSzUsername(String szUsername) {
        this.szUsername = szUsername;
    }

    public byte getChStatus() {
        return chStatus;
    }

    public void setChStatus(byte chStatus) {
        this.chStatus = chStatus;
    }

    public byte getChUserType() {
        return chUserType;
    }

    public void setChUserType(byte chUserType) {
        this.chUserType = chUserType;
    }

    public byte[] toByte() {
        byte[] b = new byte[52];
        byte[] temp = new byte[50];
        System.arraycopy(getSzUsername().getBytes(), 0, b, 0, getSzUsername().length());
        temp = ParseUtil.toLH(getChStatus());
        System.arraycopy(temp, 0, b, 50, temp.length);
        temp = ParseUtil.toLH(getChUserType());
        System.arraycopy(temp, 0, b, 51, temp.length);
        return b;
    }

}