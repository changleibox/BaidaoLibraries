/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.box.app.library.IContext;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by box on 2017/12/28.
 * <p>
 * 配置
 */

interface HttpConfigDelegate {

    <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer);

    <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Action1<T> observer, boolean isShowPrompt);

    <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer);

    <T> Subscriber<T> request(@Nullable IContext iContext, HttpRequest request, @NonNull Observable<T> observable, @Nullable Observer<T> observer, boolean isShowPrompt);
}
