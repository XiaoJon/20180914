package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/8.
 * Describe：
 */

public class ImDataBean implements Serializable {
    private byte[] bytes;
    private QxMessage message;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public QxMessage getMessage() {
        return message;
    }

    public void setMessage(QxMessage message) {
        this.message = message;
    }
}
