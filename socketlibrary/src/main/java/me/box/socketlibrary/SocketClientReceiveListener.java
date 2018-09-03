/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientReceivingDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

/**
 * Created by box on 2017/8/10.
 * <p>
 * 接受消息状态监听
 */

class SocketClientReceiveListener extends SocketClientReceivingDelegate.SimpleSocketClientReceiveDelegate {

    private static final SocketClientReceiveListener instance = new SocketClientReceiveListener();

    static SocketClientReceiveListener getInstance() {
        return instance;
    }

    private SocketClientReceiveListener() {
    }

    @Override
    public void onReceivePacketBegin(SocketClient client, SocketResponsePacket packet) {
    }

    @Override
    public void onReceivePacketEnd(SocketClient client, SocketResponsePacket packet) {
    }

    @Override
    public void onReceivePacketCancel(SocketClient client, SocketResponsePacket packet) {
    }

    @Override
    public void onReceivingPacketInProgress(SocketClient client, SocketResponsePacket packet, float progress, int receivedLength) {
    }

}
