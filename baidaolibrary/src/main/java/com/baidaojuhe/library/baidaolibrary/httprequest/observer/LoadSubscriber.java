/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.observer;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.box.app.library.IContext;
import net.box.app.library.impl.IProgressImpl;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Box on 17/3/15.
 * <p>
 * load的显示
 */
public class LoadSubscriber<T> extends Subscriber<T> implements DialogInterface.OnCancelListener {

    private IContext mIContext;
    @Nullable
    private Observer<T> mObserver;
    @Nullable
    private Action1<T> mOnNext;

    private IProgressImpl mProgress;

    public LoadSubscriber(@NonNull IContext iContext, @Nullable Action1<T> onNext) {
        this.mIContext = iContext;
        this.mOnNext = onNext;
    }

    public LoadSubscriber(@NonNull IContext iContext, @Nullable Observer<T> observer) {
        this.mIContext = iContext;
        this.mObserver = observer;

        if (observer != null && observer instanceof Subscriber) {
            ((Subscriber<T>) observer).add(new Subscription() {
                @Override
                public void unsubscribe() {
                    LoadSubscriber.this.unsubscribe();
                    mIContext.loadDismiss();
                }

                @Override
                public boolean isUnsubscribed() {
                    return LoadSubscriber.this.isUnsubscribed();
                }
            });
        }
    }

    @Override
    public void onStart() {
        mProgress = mIContext.showLoad();
        if (mProgress != null) {
            mProgress.show();
            mProgress.setOnCancelListener(this);
        }
        if (mObserver != null && mObserver instanceof Subscriber) {
            ((Subscriber) mObserver).onStart();
        }
    }

    @Override
    public void onCompleted() {
        requestCompleted();
        if (mObserver != null) {
            mObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        requestCompleted();
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onNext(T o) {
        requestCompleted();
        if (mObserver != null) {
            mObserver.onNext(o);
        }
        if (mOnNext != null) {
            mOnNext.call(o);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!isUnsubscribed()) {
            unsubscribe();
            onCompleted();
        }
    }

    private void requestCompleted() {
        mIContext.loadDismiss();
        if (mProgress != null) {
            mProgress.setOnCancelListener(null);
            mProgress = null;
        }
    }
}
