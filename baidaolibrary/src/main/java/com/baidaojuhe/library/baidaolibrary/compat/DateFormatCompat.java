/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;

import net.box.app.library.common.IConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by box on 2017/8/29.
 * <p>
 * 格式化时间
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class DateFormatCompat {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final long ND = 1000 * 24 * 60 * 60;//一天的毫秒数
    private static final long NH = 1000 * 60 * 60;//一小时的毫秒数
    private static final long NM = 1000 * 60;//一分钟的毫秒数
    private static final long NS = 1000;

    @Nullable
    public static Date parse(CharSequence date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @Nullable
    public static Date parseYMD(CharSequence date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static CharSequence formatMD(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("MM-dd", date);
    }

    public static CharSequence formatYM(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("yyyy-MM", date);
    }

    public static CharSequence formatYMD(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("yyyy-MM-dd", date);
    }

    public static CharSequence formatYMDHM(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("yyyy-MM-dd HH:mm", date);
    }

    public static CharSequence formatYMDHMS(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", date);
    }

    public static CharSequence formatHM(Date date) {
        if (!checkNotNull(date)) {
            return IConstants.EMPTY;
        }
        return DateFormat.format("HH:mm", date);
    }

    public static CharSequence formatMD(CharSequence date) {
        return formatMD(parse(date));
    }

    public static CharSequence formatYM(CharSequence date) {
        return formatYM(parse(date));
    }

    public static CharSequence formatYMD(CharSequence date) {
        return formatYMD(parse(date));
    }

    public static CharSequence formatYMDHM(CharSequence date) {
        return formatYMDHM(parse(date));
    }

    public static CharSequence formatYMDHMS(CharSequence date) {
        return formatYMDHMS(parse(date));
    }

    public static CharSequence formatHM(CharSequence date) {
        return formatHM(parse(date));
    }

    private static boolean checkNotNull(Date date) {
        return date != null;
    }
}
