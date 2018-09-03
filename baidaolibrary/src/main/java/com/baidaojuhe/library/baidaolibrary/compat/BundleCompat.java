/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;

import java.io.Serializable;
import java.util.List;

/**
 * Created by box on 2017/9/26.
 * <p>
 * 值获取
 */

@SuppressWarnings("unchecked")
public final class BundleCompat {

    public static final Bundle BUNDLE_MODALITY = new Bundle();

    static {
        BUNDLE_MODALITY.putBoolean(BDConstants.BDKey.KEY_IS_MODALITY, true);
    }

    public static <T extends Serializable> T getSerializable(@NonNull IContext context, String key) {
        return (T) context.getBundle().getSerializable(key);
    }

    public static <T extends Serializable> T getSerializable(@NonNull Intent data, String key) {
        return (T) data.getSerializableExtra(key);
    }

    public static <T extends Serializable> T getSerializable(@NonNull Bundle data, String key) {
        return (T) data.getSerializable(key);
    }

    public static <T extends Parcelable> T getParcelable(@NonNull IContext context, String key) {
        return context.getBundle().getParcelable(key);
    }

    public static <T extends Parcelable> List<T> getParcelables(@NonNull IContext context, String key) {
        return context.getBundle().getParcelableArrayList(key);
    }

    public static <T extends Parcelable> T getParcelable(@NonNull Intent data, String key) {
        return data.getParcelableExtra(key);
    }

    public static <T extends Parcelable> List<T> getParcelables(@NonNull Intent data, String key) {
        return data.getParcelableArrayListExtra(key);
    }

    public static <T extends Parcelable> T getParcelable(@NonNull Bundle data, String key) {
        return data.getParcelable(key);
    }

    public static <T extends Parcelable> List<T> getParcelables(@NonNull Bundle data, String key) {
        return data.getParcelableArrayList(key);
    }
}
