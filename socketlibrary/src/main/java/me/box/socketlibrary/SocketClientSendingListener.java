/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientSendingDelegate;
import com.vilyever.socketclient.helper.SocketPacket;

/**
 * Created by box on 2017/8/10.
 * <p>
 * 发送消息监听
 */

class SocketClientSendingListener extends SocketClientSendingDelegate.SimpleSocketClientSendingDelegate {

    private static final SocketClientSendingListener instance = new SocketClientSendingListener();

    static SocketClientSendingListener getInstance() {
        return instance;
    }

    private SocketClientSendingListener() {
    }

    @Override
    public void onSendPacketBegin(SocketClient client, SocketPacket packet) {
    }

    @Override
    public void onSendPacketEnd(SocketClient client, SocketPacket packet) {
    }

    @Override
    public void onSendPacketCancel(SocketClient client, SocketPacket packet) {
    }

    @Override
    public void onSendingPacketInProgress(SocketClient client, SocketPacket packet, float progress, int sendedLength) {
    }

}
