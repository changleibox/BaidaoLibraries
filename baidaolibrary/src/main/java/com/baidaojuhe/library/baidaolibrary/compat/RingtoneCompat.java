/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by box on 2017/7/20.
 * <p>
 * 播放系统铃声
 */

public class RingtoneCompat {

    private static Uri sRingtoneUri;
    private static Ringtone sRingtone;

    @SuppressWarnings("deprecation")
    public static void play(Context context) {
        if (sRingtoneUri == null) {
            sRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        }
        if (sRingtoneUri != null && sRingtone == null) {
            sRingtone = RingtoneManager.getRingtone(context, sRingtoneUri);
        }

        if (sRingtone != null) {
            if (sRingtone.isPlaying()) {
                sRingtone.stop();
            }
            sRingtone.setStreamType(AudioManager.STREAM_NOTIFICATION);
            sRingtone.play();
        }
    }
}
