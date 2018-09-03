/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.text.TextUtils;
import android.util.Base64;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import net.box.app.library.helper.IAppHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by box on 2017/4/8.
 * <p>
 * 处理prefrence缓存
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class PrefrenceHelper {

    private final SharedPreferences mPreferences;

    public PrefrenceHelper(String name) {
        mPreferences = IAppHelper.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public <T> void put(String key, T value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

    public void put(String key, Set<String> set) {
        mPreferences.edit().putStringSet(key, set).apply();
    }

    public <T extends Serializable> void putSerializables(String key, List<T> objects) {
        put(key, Stream.of(objects).map(value -> {
            try {
                return serializableToBase64(value);
            } catch (IOException ignored) {
            }
            return null;
        }).filter(value -> !TextUtils.isEmpty(value)).collect(Collectors.toSet()));
    }

    public <T extends Serializable> List<T> getSerializables(String key) {
        List<T> list = new ArrayList<>();
        //noinspection unchecked
        Stream.of(get(key)).map(value -> {
            try {
                return base64ToSerializable(value);
            } catch (IOException | ClassNotFoundException ignored) {
            }
            return null;
        }).filter(BDUtils::nonNull).forEach(value -> list.add((T) value));
        return list;
    }

    public <T extends Parcelable> void putParcelables(String key, List<T> objects) {
        put(key, Stream.of(objects).map(PrefrenceHelper::parcelableToBase64).collect(Collectors.toSet()));
    }

    public <T extends Parcelable> List<T> getParcelables(String key, Parcelable.Creator<T> creator) {
        return Stream.of(get(key)).map(value -> base64ToParcelable(value, creator)).collect(Collectors.toList());
    }

    public void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

    public boolean get(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return mPreferences.getFloat(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public String get(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    public Set<String> get(String key, @Nullable Set<String> defValues) {
        return mPreferences.getStringSet(key, defValues == null ? new ArraySet<>() : defValues);
    }

    public Set<String> get(String key) {
        return get(key, (Set<String>) null);
    }

    public static String serializableToBase64(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public static Serializable base64ToSerializable(String base64) throws IOException, ClassNotFoundException {
        return base64ToSerializable(Base64.decode(base64, Base64.DEFAULT));
    }

    public static Serializable base64ToSerializable(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Serializable) ois.readObject();
    }

    public static String parcelableToBase64(Parcelable object) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        object.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static <T extends Parcelable> T base64ToParcelable(String base64, Parcelable.Creator<T> creator) {
        if (TextUtils.isEmpty(base64)) {
            return null;
        }
        return base64ToParcelable(Base64.decode(base64, Base64.DEFAULT), creator);
    }

    public static <T extends Parcelable> T base64ToParcelable(byte[] bytes, Parcelable.Creator<T> creator) {
        if (bytes == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return creator.createFromParcel(parcel);
    }

}
