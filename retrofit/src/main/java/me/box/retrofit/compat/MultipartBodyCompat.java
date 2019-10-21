/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.compat;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import me.box.retrofit.annotation.MultipartFile;
import me.box.retrofit.util.HttpUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by box on 2019-10-18.
 * <p>
 * MultipartBody兼容类
 */
@SuppressWarnings({"unused"})
public class MultipartBodyCompat {

    public static MultipartBody charSequenceToMultipartBody(@NonNull String name, @Nullable CharSequence charSequence) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, charSequence, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody booleanToMultipartBody(@NonNull String name, boolean b) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, b, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody numberToMultipartBody(@NonNull String name, @Nullable Number number) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, number, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody characterToMultipartBody(@NonNull String name, char c) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, c, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody requestBodyToMultipartBody(@NonNull String name, @Nullable RequestBody body) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, body, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody fileToMultipartBody(@NonNull String name, @Nullable File file) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, file, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody arrayToMultipartBody(@NonNull String name, @Nullable Object[] objects) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, objects, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody listToMultipartBody(@NonNull String name, @Nullable List list) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, list, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody mapToMultipartBody(@NonNull String name, @Nullable Map map) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            baseDataAddToMultipartBody(name, map, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static MultipartBody objectToMultipartBody(@Nullable Object obj) {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            objectAddToMultipartBody(null, obj, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    private static void objectAddToMultipartBody(@Nullable String name, @Nullable Object obj, @NonNull MultipartBody.Builder builder) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
        final Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            final SerializedName serializedName = field.getAnnotation(SerializedName.class);
            final MultipartFile multipartFile = field.getAnnotation(MultipartFile.class);
            final String fieldName = serializedName == null ? field.getName() : serializedName.value();
            final Object value = field.get(obj);
            final int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || value == null) {
                continue;
            }
            final String currentName = TextUtils.isEmpty(name) ? fieldName : name + "." + fieldName;
            if (!baseDataAddToMultipartBody(currentName, value, multipartFile, builder)) {
                objectAddToMultipartBody(currentName, value, builder);
            }
        }
    }

    private static void baseDataAddToMultipartBody(@NonNull String name, @Nullable Object obj, @NonNull MultipartBody.Builder builder) throws IllegalAccessException {
        baseDataAddToMultipartBody(name, obj, null, builder);
    }

    private static boolean baseDataAddToMultipartBody(@NonNull String name, @Nullable Object obj, @Nullable MultipartFile multipartFile, @NonNull MultipartBody.Builder builder) throws IllegalAccessException {
        if (multipartFile != null && obj instanceof String) {
            final MultipartFile.Type type = multipartFile.value();
            switch (type) {
                case PATH:
                    return baseDataAddToMultipartBody(name, new File(obj.toString()), multipartFile, builder);
                case BASE64:
                    return baseDataAddToMultipartBody(name, HttpUtils.getBase64RequestBody(obj.toString()), multipartFile, builder);
            }
        } else if (obj instanceof CharSequence
                // || value instanceof Integer
                // || value instanceof Double
                // || value instanceof Byte
                // || value instanceof Short
                // || value instanceof Long
                // || value instanceof Float
                || obj instanceof Boolean
                || obj instanceof Number) {
            builder.addFormDataPart(name, obj.toString());
            return true;
        } else if (obj instanceof Character) {
            builder.addFormDataPart(name, Character.toString((Character) obj));
            return true;
        } else if (obj instanceof RequestBody) {
            builder.addFormDataPart(name, null, (RequestBody) obj);
            return true;
        } else if (obj instanceof File) {
            builder.addFormDataPart(name, ((File) obj).getName(), HttpUtils.getFileRequestBody((File) obj));
            return true;
        } else if (obj instanceof Object[]) {
            for (int i = 0; i < ((Object[]) obj).length; i++) {
                final String currentName = String.format("%s[%s]", name, i);
                final Object value = ((Object[]) obj)[i];
                if (!baseDataAddToMultipartBody(currentName, value, multipartFile, builder)) {
                    objectAddToMultipartBody(currentName, value, builder);
                }
            }
            return true;
        } else if (obj instanceof List) {
            for (int i = 0; i < ((List) obj).size(); i++) {
                final String currentName = String.format("%s[%s]", name, i);
                final Object value = ((List) obj).get(i);
                if (!baseDataAddToMultipartBody(currentName, value, multipartFile, builder)) {
                    objectAddToMultipartBody(currentName, value, builder);
                }
            }
            return true;
        } else if (obj instanceof Map) {
            for (Object key : ((Map) obj).keySet()) {
                final String currentName = String.format("%s.%s", name, key.toString());
                final Object value = ((Map) obj).get(key);
                if (!baseDataAddToMultipartBody(currentName, value, multipartFile, builder)) {
                    objectAddToMultipartBody(currentName, value, builder);
                }
            }
            return true;
        }
        return false;
    }
}
