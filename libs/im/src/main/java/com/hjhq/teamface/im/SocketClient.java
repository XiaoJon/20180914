package com.hjhq.teamface.im;

import android.util.Log;

import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.util.EventBusUtils;
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

public class SocketClient extends WebSocketClient {
    String TAG = "SocketClient";

    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    public SocketClient(URI serverUri, SocketStateListener listener) {
        super(serverUri);
        this.mListener = listener;
    }

    public SocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public SocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        Log.e(TAG, "SocketClient打开");

        if (!IMState.getImOnlineState() && IMState.isImCanLogin()) {
            if (Constants.USE_LOAD_LEVELING) {
                if (IMState.isImConnectLlOk()) {
                    IM.getInstance().login();
                }
            } else {
                IM.getInstance().login();
                Log.e(TAG, "SocketClient打开登录");
            }


        }

        if (mListener != null) {
            mListener.onOpen(MsgConstant.IM_SOCKET, serverHandshake);
        }
    }

    @Override
    public void onMessage(String s) {
        Log.e(TAG, "SocketClient收到字符串消息====" + s);

    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        ParseUtil.parseMessage(bytes);
        // Log.e(TAG, "SocketClient收到字节消息,字节数====" + bytes.array().length);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.e(TAG, "SocketClient关闭");
        if (mListener != null) {
            mListener.onClose(1);
        }
        EventBusUtils.sendEvent(new ImMessage(1, MsgConstant.IM_OFFLINE_TAG, ""));
        IMState.setImOnlineState(false);
        // IMState.setImCanLogin(true);
        IMState.setImConnectLlOk(false);
    }

    @Override
    public void onError(Exception e) {
        EventBusUtils.sendEvent(new ImMessage(1, MsgConstant.IM_OFFLINE_TAG, ""));
        Log.e("SocketClient错误", "SocketClient错误");
        if (mListener != null) {
            mListener.onError(e);
        }
        IMState.setImOnlineState(false);
        // IMState.setImCanLogin(true);
        IMState.setImConnectLlOk(false);
    }


    SocketStateListener mListener;

    public void setSocketStateChangeListener(SocketStateListener listener) {
        this.mListener = listener;
    }


}
