/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.observer;

import android.support.annotation.Nullable;

import me.box.retrofit.impl.OnShowErrorMsgListener;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by box on 2017/5/2.
 * <p>
 * 基本的网络请求监听者
 */

@SuppressWarnings("unused")
public class ISubscriber<T> extends Subscriber<T> {

    @Nullable
    private Observer<T> mObserver;
    private boolean isShowPrompt;

    @Nullable
    private static OnShowErrorMsgListener sShowErrorMsgListener;

    public ISubscriber(@Nullable Observer<T> observer) {
        this(observer, true);
    }

    public ISubscriber(@Nullable Observer<T> observer, boolean isShowPrompt) {
        this.mObserver = observer;
        this.isShowPrompt = isShowPrompt;
        if (observer instanceof Subscriber) {
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
        if (mObserver instanceof Subscriber) {
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

    public static void setShowErrorMsgListener(OnShowErrorMsgListener showErrorMsgListener) {
        ISubscriber.sShowErrorMsgListener = showErrorMsgListener;
    }

    private static void showErrorMsg(Throwable e) {
        if (sShowErrorMsgListener != null) {
            sShowErrorMsgListener.onShowErrorMsg(e);
        }
    }
}
