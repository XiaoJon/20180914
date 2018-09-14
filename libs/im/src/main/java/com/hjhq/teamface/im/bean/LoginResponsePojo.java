package com.hjhq.teamface.im.bean;

/**
 * 
 *@Title:
 *@Description: 登录响应
 *@Author:chenxiaomin
 *@Since:2017年10月11日
 *@Version:1.1.0
 */
public class LoginResponsePojo
{
    /** 为0是没有错误,不为0是错误编码 */
    private String iErrorNum;

    public String getiErrorNum()
    {
        return iErrorNum;
    }

    public void setiErrorNum(String iErrorNum)
    {
        this.iErrorNum = iErrorNum;
    }
}