package com.hjhq.teamface.im.bean;

/**
 * @Description:
 * @author: chenxiaomin
 * @date: 2017年10月11日 下午2:25:49
 * @version: 1.0
 */
public class MsgPojo {
    /**
     * 发送者ID号,登录请求包可以不填充
     */
    private long senderID;

    /**
     * 接收者ID.当群发的时候为群ID
     */
    private long receiverID;

    /**
     * 客户端填充时间戳,微秒
     */
    private long clientTimes;

    /*服务端时间戳*/
    private long serverTimes;

    /**
     * 随机数
     */
    private int rand;

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(long receiverID) {
        this.receiverID = receiverID;
    }

    public long getClientTimes() {
        return clientTimes;
    }

    public void setClientTimes(long clientTimes) {
        this.clientTimes = clientTimes;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public long getServerTimes() {
        return serverTimes;
    }

    public void setServerTimes(long serverTimes) {
        this.serverTimes = serverTimes;
    }
}

    