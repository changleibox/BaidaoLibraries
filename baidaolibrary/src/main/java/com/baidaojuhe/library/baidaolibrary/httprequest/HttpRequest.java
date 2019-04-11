/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.httprequest.observer.LoadSubscriber;
import com.baidaojuhe.library.baidaolibrary.httprequest.retrofit.IGsonConverterFactory;
import com.baidaojuhe.library.baidaolibrary.httprequest.util.HttpUtils;

import net.box.app.library.IContext;
import net.box.app.library.util.ILogCompat;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Box on 17/3/16.
 * <p>
 * 网络请求
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HttpRequest {

    private static final String TAG = HttpRequest.class.getSimpleName();

    private Retrofit mRetrofit;

    public HttpRequest(@NonNull Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }

    public HttpRequest(@NonNull String baseUrl, @IntRange(from = 1, to = Integer.MAX_VALUE) long timeout) {
        this(baseUrl, timeout, LOGGER);
    }

    public HttpRequest(@NonNull String baseUrl, @IntRange(from = 1, to = Integer.MAX_VALUE) long timeout, @Nullable Interceptor interceptor) {
        this(baseUrl, timeout, LOGGER, interceptor);
    }

    public HttpRequest(@NonNull String baseUrl, @IntRange(from = 1, to = Integer.MAX_VALUE) long timeout, @Nullable Logger logger) {
        this(baseUrl, timeout, logger, null);
    }

    public HttpRequest(@NonNull String baseUrl, @IntRange(from = 1, to = Integer.MAX_VALUE) long timeout, @Nullable Logger logger, @Nullable Interceptor interceptor) {
        this(buildOkHttpClient(timeout, logger, interceptor), baseUrl);
    }

    public HttpRequest(@NonNull OkHttpClient client, @NonNull String baseUrl) {
        this(buildRetrofit(client, baseUrl));
    }

    public <T> T create(@NonNull final Class<T> cls) {
        return mRetrofit.create(cls);
    }

    public <T> Subscriber<T> request(@Nullable IContext iContext, @NonNull Observable<T> observable, @Nullable Observer<T> observer) {
        Subscriber<T> subscriber;
        if (observer instanceof LoadSubscriber) {
            subscriber = (LoadSubscriber<T>) observer;
        } else if (iContext != null) {
            subscriber = new LoadSubscriber<>(iContext, observer);
        } else if (observer instanceof Subscriber) {
            subscriber = (Subscriber<T>) observer;
        } else {
            subscriber = HttpUtils.convertToSubscriber(observer);
        }
        return subscribe(observable, subscriber);
    }

    public <T> Subscriber<T> request(@NonNull Observable<T> observable, @Nullable Observer<T> subscriber) {
        return request(null, observable, subscriber);
    }

    public <T> Subscriber<T> request(@Nullable IContext iContext, @NonNull Observable<T> observable, @Nullable Action1<T> onNext) {
        Subscriber<T> subscriber;
        if (iContext != null) {
            subscriber = new LoadSubscriber<>(iContext, onNext);
        } else {
            subscriber = HttpUtils.convertToSubscriber(onNext);
        }
        return subscribe(observable, subscriber);
    }

    public <T> Subscriber<T> request(@NonNull Observable<T> observable, @Nullable Action1<T> onNext) {
        return request(null, observable, onNext);
    }

    private <T> Subscriber<T> subscribe(@NonNull Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscriber;
    }

    private static Retrofit buildRetrofit(@NonNull OkHttpClient client, @NonNull String baseUrl) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(IGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    private static OkHttpClient buildOkHttpClient(@IntRange(from = 1, to = Integer.MAX_VALUE) long timeout, @Nullable Logger logger, @Nullable Interceptor interceptor) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        httpClientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        httpClientBuilder.writeTimeout(timeout, TimeUnit.MILLISECONDS);

        if (interceptor != null) {
            httpClientBuilder.addInterceptor(interceptor);
        }

        if (logger != null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        return httpClientBuilder.build();
    }

    private static final Logger LOGGER = message -> {
        //打印retrofit日志
        ILogCompat.i(TAG, message);
    };
}
