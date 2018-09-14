package com.hjhq.teamface.im;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by Administrator on 2018/6/29.
 * Describe：
 */

public interface SocketStateListener {
    void onOpen(int socketType, ServerHandshake serverHandshake);

    void onError(Exception e);

    void onClose(int type);
}
