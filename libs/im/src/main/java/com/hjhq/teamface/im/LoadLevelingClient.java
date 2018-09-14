package com.hjhq.teamface.im;

import android.util.Log;

import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.im.util.ParseUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/21.
 * Describe：
 */

public class LoadLevelingClient extends WebSocketClient {
    String TAG = "LoadLevelingClient";

    public LoadLevelingClient(URI serverUri) {
        super(serverUri);
    }

    public LoadLevelingClient(URI serverUri, SocketStateListener listener) {
        super(serverUri);
        this.mListener = listener;
    }

    public LoadLevelingClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public LoadLevelingClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        Log.e(TAG, "LoadLevelingClient打开");
        if (mListener != null) {
            mListener.onOpen(MsgConstant.LL_SOCKET, serverHandshake);
        }
    }

    @Override
    public void onMessage(String s) {
        Log.e(TAG, "LoadLevelingClient收到字符串消息====" + s);

    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        ParseUtil.parseUrl(bytes);
       // Log.e(TAG, "LoadLevelingClient收到字节消息,字节数====" + bytes.array().length);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.e(TAG, "LoadLevelingClient关闭");
        if (mListener != null) {
            mListener.onClose(2);
        }
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "LoadLevelingClient错误");
        if (mListener != null) {
            mListener.onError(e);
        }
    }

    SocketStateListener mListener;

    public void setSocketStateChangeListener(SocketStateListener listener) {
        this.mListener = listener;
    }


}
