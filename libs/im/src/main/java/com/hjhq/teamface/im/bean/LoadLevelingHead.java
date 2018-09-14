package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.im.util.ParseUtil;


/**
 * @Description: 命令包头
 * @author: chenxiaomin
 * @date: 2017年10月11日 下午2:24:08
 * @version: 1.0
 */
public class LoadLevelingHead {
    private int netFlag;
    private byte state;
    private long imId;
    private byte deviceType;

    public int getNetFlag() {
        return netFlag;
    }

    public void setNetFlag(int netFlag) {
        this.netFlag = netFlag;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getImId() {
        return imId;
    }

    public void setImId(long imId) {
        this.imId = imId;
    }

    public byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    public byte[] toByte() {
        byte[] b = new byte[14];
        byte[] temp;
        // int 4字节数组
        temp = ParseUtil.longToBytes(getNetFlag());
        System.arraycopy(temp, 0, b, 0, temp.length);

        temp = ParseUtil.toLH(getState());
        System.arraycopy(temp, 0, b, 4, temp.length);

        temp = ParseUtil.longToBytes(getImId());
        System.arraycopy(temp, 0, b, 5, temp.length);

        temp = ParseUtil.toLH(getDeviceType());
        System.arraycopy(temp, 0, b, 13, temp.length);


        return b;
    }
}

    