/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import static me.box.socketlibrary.SocketHelper.debug;
import static me.box.socketlibrary.SocketHelper.error;

/**
 * Created by box on 2017/8/10.
 * <p>
 * 消息
 */

@SuppressWarnings("WeakerAccess")
abstract class SimpleSocketClient extends SocketClientDelegate.SimpleSocketClientDelegate implements Handler.Callback, SocketCallback {

    private static final int WHAT_RECONNECT = 0x0;
    private static final long RECONNECT_DELAYED = 3 * 1000L;

    @Nullable
    private Handler mHandler;
    private int mDisconnectCount;

    protected SimpleSocketClient() {
    }

    @Override
    public final void onConnected(SocketClient client) {
        debug("Socket连接成功");

        mDisconnectCount = 0;
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper(), this);
        }
        mHandler.removeMessages(WHAT_RECONNECT);

        onConnected();
    }

    @Override
    public final void onDisconnected(SocketClient client) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(WHAT_RECONNECT);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(WHAT_RECONNECT, client), RECONNECT_DELAYED);

        onDisconnected();
    }

    @Override
    public final void onResponse(final SocketClient client, @NonNull SocketResponsePacket responsePacket) {
        boolean heartBeat = responsePacket.isHeartBeat();
        if (heartBeat) {
            return;
        }
        onResponse(responsePacket);
    }

    @Override
    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case WHAT_RECONNECT:
                ((SocketClient) message.obj).connect();
                error("Socket连接失败，正在尝试重新连接，连接次数：" + (++mDisconnectCount));
                onReconnected();
                break;
        }
        return true;
    }

    final void disconnect() {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(WHAT_RECONNECT);
        mHandler = null;
    }

}
