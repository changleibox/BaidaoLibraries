/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.box.retrofit.common.Constants;
import me.box.retrofit.common.Constants.Config;
import me.box.retrofit.impl.LifecycleImpl;
import me.box.retrofit.impl.RetrofitContext;
import me.box.retrofit.observer.Callback;
import me.box.retrofit.observer.ISubscriber;
import okhttp3.Interceptor;
import okhttp3.Request;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2017/8/8.
 * <p>
 * 网络请求配置
 */

@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public abstract class HttpConfig {

    protected HttpConfig() {
    }

    protected static Interceptor createBaseInterceptor(String token) {
        return chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("token", token == null ? Constants.EMPTY : token)
                    .addHeader("tokenType", "Android")
                    .addHeader("contentType", "application/json;charset=utf-8")
                    .addHeader("Accept", "application/json");
            return chain.proceed(builder.build());
        };
    }

    protected static <T> Subscriber<T> request(@Nullable RetrofitContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer) {
        return request(iContext, request, observable, observer, true);
    }

    protected static <T> Subscriber<T> request(@Nullable RetrofitContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer, boolean isShowPrompt) {
        return request(iContext, request, observable, Callback.inclusion(observer), isShowPrompt);
    }

    protected static <T> Subscriber<T> request(@Nullable RetrofitContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer) {
        return request(iContext, request, observable, observer, true);
    }

    protected static <T> Subscriber<T> request(@Nullable RetrofitContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer, boolean isShowPrompt) {
        if (iContext != null && iContext instanceof LifecycleImpl) {
            observable = observable.compose(((LifecycleImpl) iContext).bindToLifecycle());
        }
        Subscriber<T> subscriber = request.request(iContext, observable, new ISubscriber<>(observer, isShowPrompt));
        CompositeSubscription subscription;
        if (iContext != null && iContext instanceof LifecycleImpl && (subscription = ((LifecycleImpl) iContext).getCompositeSubscription()) != null) {
            subscription.add(subscriber);
        }
        return subscriber;
    }

    protected static int formatPageNum(int pageNum) {
        return pageNum < Config.FIRST_PAGE ? Config.FIRST_PAGE : pageNum;
    }

}
