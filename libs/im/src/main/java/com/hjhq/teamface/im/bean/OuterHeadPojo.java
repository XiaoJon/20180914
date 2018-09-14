package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.im.util.ParseUtil;

/**
* @Description: 外层包头
* @author: chenxiaomin 
* @date: 2017年10月11日 下午2:19:18 
* @version: 1.0 
*/
public class OuterHeadPojo
{
    /** 包的标识 */
    private int iPacketFlag;
    
    /** 数据长度 */
    private int iDataLen;

    public int getiPacketFlag()
    {
        return iPacketFlag;
    }

    public void setiPacketFlag(int iPacketFlag)
    {
        this.iPacketFlag = iPacketFlag;
    }

    public int getiDataLen()
    {
        return iDataLen;
    }

    public void setiDataLen(int iDataLen)
    {
        this.iDataLen = iDataLen;
    }
    
    public byte[] toByte()
    {
        byte[] b = new byte[8];
        byte[] temp;
        // int 4字节数组
        temp = ParseUtil.toLH(getiPacketFlag());
        System.arraycopy(temp, 0, b, 0, temp.length);
        
        // int 4字节数组
        temp = ParseUtil.toLH(getiDataLen());
        System.arraycopy(temp, 0, b, 4, temp.length);
        return b;
    }
    
    /**
     * 包头信息
     * 
     * @param b
     * @return
     */
    public static OuterHeadPojo getOuterHeadInfo(byte[] b)
    {
        OuterHeadPojo headPojo = new OuterHeadPojo();
        byte[] temp = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            temp[i] = b[i + 0];
        }
        headPojo.setiPacketFlag(ParseUtil.lBytesToInt(temp));// int 4字节数组
        
        for (int i = 0; i < 4; i++)
        {
            temp[i] = b[i + 4];
        }
        headPojo.setiDataLen(ParseUtil.lBytesToInt(temp)); // int 4字节数组
        return headPojo;
    }
}

    