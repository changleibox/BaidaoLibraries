/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.httprequest.observer.Callback;
import com.baidaojuhe.library.baidaolibrary.httprequest.observer.ISubscriber;
import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;

import net.box.app.library.IContext;
import net.box.app.library.common.IConstants;

import okhttp3.Interceptor;
import okhttp3.Request;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDConfig.FIRST_PAGE;

/**
 * Created by box on 2017/8/8.
 * <p>
 * 网络请求配置
 */

@SuppressWarnings("WeakerAccess")
public abstract class BDHttpConfig {

    protected BDHttpConfig() {
    }

    protected static Interceptor createBaseInterceptor(String token) {
        return chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("token", token == null ? IConstants.EMPTY : token)
                    .addHeader("tokenType", "Android")
                    .addHeader("contentType", "application/json;charset=utf-8")
                    .addHeader("Accept", "application/json");
            return chain.proceed(builder.build());
        };
    }

    protected static <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer) {
        return request(iContext, request, observable, observer, true);
    }

    protected static <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer, boolean isShowPrompt) {
        return request(iContext, request, observable, Callback.inclusion(observer), isShowPrompt);
    }

    protected static <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer) {
        return request(iContext, request, observable, observer, true);
    }

    protected static <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer, boolean isShowPrompt) {
        if (iContext != null && iContext instanceof ContextExtend) {
            observable = observable.compose(((ContextExtend) iContext).bindToLifecycle());
        }
        Subscriber<T> subscriber = request.request(iContext, observable, new ISubscriber<>(observer, isShowPrompt));
        CompositeSubscription subscription;
        if (iContext != null && iContext instanceof ContextExtend
                && (subscription = ((ContextExtend) iContext).getCompositeSubscription()) != null) {
            subscription.add(subscriber);
        }
        return subscriber;
    }

    protected static int formatPageNum(int pageNum) {
        return pageNum < FIRST_PAGE ? FIRST_PAGE : pageNum;
    }

}
