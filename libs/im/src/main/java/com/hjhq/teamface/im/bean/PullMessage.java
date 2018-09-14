package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.im.util.ParseUtil;

/**
 * @Description:拉取历史消息传参结构体
 */
public class PullMessage {
    //消息类型 1单聊 2群聊
    private byte msgType;
    //群或个人id
    private long id;
    //最早一条消息时间戳
    private long timeStamp;
    //拉取数量
    private short num;

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public byte[] toByte() {
        byte[] b = new byte[19];
        byte[] temp;
        // int 4字节数组


        temp = ParseUtil.toLH(getMsgType());
        System.arraycopy(temp, 0, b, 0, temp.length);


        temp = ParseUtil.longToBytes(getId());
        System.arraycopy(temp, 0, b, 1, temp.length);

        temp = ParseUtil.longToBytes(getTimeStamp());
        System.arraycopy(temp, 0, b, 9, temp.length);

        temp = ParseUtil.toLH(getNum());
        System.arraycopy(temp, 0, b, 17, temp.length);


        return b;
    }


}

    