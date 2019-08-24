/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import androidx.annotation.NonNull;

import com.vilyever.socketclient.helper.SocketResponsePacket;

/**
 * Created by box on 2017/12/27.
 * <p>
 * socket状态回调
 */

interface SocketCallback {

    void onConnected();

    void onReconnected();

    void onDisconnected();

    void onResponse(@NonNull SocketResponsePacket responsePacket);
}
