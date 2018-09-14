package com.hjhq.teamface.im.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Administrator
 * @date 2017/11/23
 * Describe：会话
 */
@Entity
public class MessageReadList implements Serializable {
    static final long serialVersionUID = 2L;
    //会话id
    @Id
    protected long clientTime;
    protected long msgId;

    long readerId;
    String companyId;
    long conversationId;


    @Keep
    public MessageReadList(long clientTime, long msgId, long readerId,
                           String companyId, long conversationId) {
        this.clientTime = clientTime;
        this.msgId = msgId;
        this.readerId = readerId;
        this.companyId = companyId;
        this.conversationId = conversationId;
    }


    public MessageReadList() {
    }


    public long getClientTime() {
        return clientTime;
    }

    public void setClientTime(long clientTime) {
        this.clientTime = clientTime;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getReaderId() {
        return readerId;
    }

    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }
}
