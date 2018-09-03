/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.socketlibrary;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketHeartBeatHelper;
import com.vilyever.socketclient.helper.SocketPacket;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.util.CharsetUtil;
import com.vilyever.socketclient.util.StringValidation;

/**
 * Created by box on 2017/8/9.
 * <p>
 * socket配置
 */

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue", "SameParameterValue"})
public final class SocketHelper {

    private static final String TAG = SocketHelper.class.getSimpleName();
    private static final long HEART_BEAT_INTERVAL = 10 * 1000L;
    private static final long SEND_TIMEOUT = 60 * 1000;
    private static final int SEND_SEGMENT_LENGTH = 8;
    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    private static final int RECEIVE_PACKET_LENGTH_DATA_LENGTH = 4;

    private static final SocketClient CLIENT = new SocketClient();

    @Nullable
    private static SimpleSocketClient sSocketClient;

    private SocketHelper() {
    }

    public static void configure(String remoteIP, int remotePort, String sendHeartBeat, String receiveHeartBeat) {
        CLIENT.setCharsetName(CharsetUtil.UTF_8);
        CLIENT.setAddress(new SocketClientAddress(remoteIP, remotePort, CONNECTION_TIMEOUT));

        SocketPacketHelper socketPacketHelper = CLIENT.getSocketPacketHelper();
        socketPacketHelper.setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadByLength);
        socketPacketHelper.setReceivePacketLengthDataLength(RECEIVE_PACKET_LENGTH_DATA_LENGTH);
        socketPacketHelper.setReceivePacketDataLengthConvertor((SocketPacketHelper helper, byte[] packetLengthData) -> {
            /*
             * 简单将 byte[]转换为 int
             */
            return byteToInt(packetLengthData);
        });
        socketPacketHelper.setSendPacketLengthDataConvertor((SocketPacketHelper helper, int packetLength) -> {
            /*
             * 简单将 int 转换为 byte[]
             */
            return intToByte(packetLength);
        });
        socketPacketHelper.setSendTimeout(SEND_TIMEOUT);
        socketPacketHelper.setSendTimeoutEnabled(true);
        socketPacketHelper.setSendSegmentLength(SEND_SEGMENT_LENGTH);
        socketPacketHelper.setSendSegmentEnabled(true);

        SocketHeartBeatHelper heartBeatHelper = CLIENT.getHeartBeatHelper();

        heartBeatHelper.setSendDataBuilder(helper -> CharsetUtil.stringToData(sendHeartBeat, CharsetUtil.UTF_8));
        heartBeatHelper.setDefaultReceiveData(CharsetUtil.stringToData(receiveHeartBeat, CharsetUtil.UTF_8));

        heartBeatHelper.setHeartBeatInterval(HEART_BEAT_INTERVAL);
        heartBeatHelper.setSendHeartBeatEnabled(true);
    }

    public static void connect(@NonNull SocketClientProvider provider) {
        checkConfigurations();
        if (CLIENT.isConnected() || CLIENT.isConnecting()) {
            return;
        }
        CLIENT.registerSocketClientSendingDelegate(SocketClientSendingListener.getInstance());
        CLIENT.registerSocketClientReceiveDelegate(SocketClientReceiveListener.getInstance());
        CLIENT.registerSocketClientDelegate(sSocketClient = provider.provide());
        CLIENT.connect();
    }

    public static void disconnect() {
        checkConfigurations();
        if (sSocketClient != null) {
            sSocketClient.disconnect();
            sSocketClient = null;
        }
        CLIENT.disconnect();
    }

    public static void setSendHeartBeatEnabled(boolean enabled) {
        checkConfigurations();
        CLIENT.getHeartBeatHelper().setSendHeartBeatEnabled(enabled);
    }

    @Nullable
    public static SocketPacket send(byte[] data) {
        checkConfigurations();
        if (data == null) {
            return null;
        }
        if (CLIENT.isDisconnected() || CLIENT.isDisconnecting()) {
            return null;
        }
        return CLIENT.sendPacket(new SocketPacket(data));
    }

    @Nullable
    public static SocketPacket send(CharSequence text) {
        checkConfigurations();
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        if (CLIENT.isDisconnected() || CLIENT.isDisconnecting()) {
            return null;
        }
        return CLIENT.sendPacket(new SocketPacket(text.toString()));
    }

    static void debug(Object msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.i(TAG, String.valueOf(msg));
    }

    static void error(Object msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.e(TAG, String.valueOf(msg));
    }

    private static void checkConfigurations() {
        SocketClientAddress address = CLIENT.getAddress();
        String remoteIP = address.getRemoteIP();
        String remotePort = address.getRemotePort();
        int connectionTimeout = address.getConnectionTimeout();
        if (TextUtils.isEmpty(remoteIP) || TextUtils.isEmpty(remotePort)) {
            throw new IllegalArgumentException("请调用SocketHelper.configure(String, int, String, String);");
        }
        if (!StringValidation.validateRegex(remoteIP, StringValidation.RegexIP)) {
            throw new IllegalArgumentException("we need a correct remote IP to connect. Current is " + remoteIP);
        }
        if (!StringValidation.validateRegex(remotePort, StringValidation.RegexPort)) {
            throw new IllegalArgumentException("we need a correct remote port to connect. Current is " + remotePort);
        }
        if (connectionTimeout < 0) {
            throw new IllegalArgumentException("we need connectionTimeout > 0. Current is " + connectionTimeout);
        }
    }

    private static byte[] intToByte(int value) {
        byte[] data = new byte[4];
        data[3] = (byte) (value & 0xFF);
        data[2] = (byte) ((value >> 8) & 0xFF);
        data[1] = (byte) ((value >> 16) & 0xFF);
        data[0] = (byte) ((value >> 24) & 0xFF);
        return data;
    }

    private static int byteToInt(byte[] data) {
        return (data[3] & 0xFF) + ((data[2] & 0xFF) << 8) + ((data[1] & 0xFF) << 16) + ((data[0] & 0xFF) << 24);
    }
}
