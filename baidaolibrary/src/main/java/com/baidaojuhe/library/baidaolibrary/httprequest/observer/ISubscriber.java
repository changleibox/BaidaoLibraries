/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.observer;

import androidx.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.ToastCompat;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by box on 2017/5/2.
 * <p>
 * 基本的网络请求监听者
 */

public class ISubscriber<T> extends Subscriber<T> {

    @Nullable
    private Observer<T> mObserver;
    private boolean isShowPrompt;

    public ISubscriber(@Nullable Observer<T> observer) {
        this(observer, true);
    }

    public ISubscriber(@Nullable Observer<T> observer, boolean isShowPrompt) {
        this.mObserver = observer;
        this.isShowPrompt = isShowPrompt;
        if (observer != null && observer instanceof Subscriber) {
            ((Subscriber<T>) observer).add(new Subscription() {
                @Override
                public void unsubscribe() {
                    ISubscriber.this.unsubscribe();
                }

                @Override
                public boolean isUnsubscribed() {
                    return ISubscriber.this.isUnsubscribed();
                }
            });
        }
    }

    @Override
    public void onStart() {
        if (mObserver != null && mObserver instanceof Subscriber) {
            ((Subscriber) mObserver).onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (mObserver != null) {
            mObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isShowPrompt) {
            showErrorMsg(e);
        }
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (mObserver != null) {
            mObserver.onNext(t);
        }
    }

    private static void showErrorMsg(Throwable e) {
        if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            ToastCompat.showText(R.string.bd_error_request_timeout);
        } else {
            ToastCompat.showText(e.getMessage());
        }
    }
}
