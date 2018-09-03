/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import android.support.annotation.NonNull;

import com.vilyever.socketclient.helper.SocketResponsePacket;

/**
 * Created by box on 2017/8/10.
 * <p>
 * 消息
 */

public abstract class SocketClientProvider implements SocketCallback {

    private SimpleSocketClient mSimpleSocketClient = new SimpleSocketClient() {
        @Override
        public void onConnected() {
            SocketClientProvider.this.onConnected();
        }

        @Override
        public void onReconnected() {
            SocketClientProvider.this.onReconnected();
        }

        @Override
        public void onDisconnected() {
            SocketClientProvider.this.onDisconnected();
        }

        @Override
        public void onResponse(@NonNull SocketResponsePacket responsePacket) {
            SocketClientProvider.this.onResponse(responsePacket);
        }
    };

    final SimpleSocketClient provide() {
        return mSimpleSocketClient;
    }

}
