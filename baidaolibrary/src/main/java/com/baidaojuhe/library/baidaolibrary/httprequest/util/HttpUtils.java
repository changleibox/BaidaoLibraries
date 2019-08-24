/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import net.box.app.library.util.IJsonDecoder;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Box on 17/3/16.
 * <p>
 * 网络请求工具类
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HttpUtils {

    private static final MediaType MEDIA_TYPE_NORMAL = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");

    @NonNull
    public static <T> Subscriber<T> convertToSubscriber(@Nullable final Action1<T> onNext) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(T t) {
                if (onNext != null) {
                    onNext.call(t);
                }
            }
        };
    }

    @NonNull
    public static <T> Subscriber<T> convertToSubscriber(@Nullable final Observer<T> observer) {
        Subscriber<T> subscriber = new Subscriber<T>() {
            @Override
            public void onCompleted() {
                if (observer != null) {
                    observer.onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (observer != null) {
                    observer.onError(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (observer != null) {
                    observer.onNext(t);
                }
            }
        };
        if (observer != null && observer instanceof Subscriber) {
            subscriber.add(new Subscription() {
                @Override
                public void unsubscribe() {
                    ((Subscriber<T>) observer).unsubscribe();
                }

                @Override
                public boolean isUnsubscribed() {
                    return ((Subscriber<T>) observer).isUnsubscribed();
                }
            });
        }
        return subscriber;
    }

    public static RequestBody getRequestBody(@NonNull Map<String, ?> map) {
        return RequestBody.create(MEDIA_TYPE_NORMAL, new JSONObject(map).toString());
    }

    public static RequestBody getRequestBody(@Nullable Object obj) {
        return RequestBody.create(MEDIA_TYPE_NORMAL, obj == null ? "" : IJsonDecoder.objectToJson(obj));
    }

    public static RequestBody getTextRequestBody(@NonNull Object value) {
        return RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(value));
    }

    public static RequestBody getFileRequestBody(@NonNull String filePath) {
        return getRequestBody(new File(filePath));
    }

    public static RequestBody getFileRequestBody(@NonNull File file) {
        return RequestBody.create(MEDIA_TYPE_FILE, file);
    }

    public static MultipartBody.Part getPart(@NonNull String name, @NonNull File file) {
        RequestBody requestBody = getFileRequestBody(file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);
    }

    public static MultipartBody.Part getPart(@NonNull String name, @NonNull String filePath) {
        return getPart(name, new File(filePath));
    }

    public static List<MultipartBody.Part> getMultipartBodyParts(@NonNull String name, @NonNull List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        parts.addAll(Stream.of(files).map(file ->
                MultipartBody.Part.createFormData(name, file.getName(), getFileRequestBody(file))).collect(Collectors.toList()));
        return parts;
    }

}
