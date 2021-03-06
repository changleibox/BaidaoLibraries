/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.observer;

import androidx.annotation.Nullable;

import rx.Observer;
import rx.functions.Action1;

public abstract class Callback<T> implements Observer<T> {

    @Nullable
    private Observer<T> mObserver;
    @Nullable
    private Action1<T> mOnNext;

    public Callback() {
    }

    public Callback(@Nullable Observer<T> observer) {
        mObserver = observer;
    }

    public Callback(@Nullable Action1<T> onNext) {
        mOnNext = onNext;
    }

    @Override
    public void onCompleted() {
        if (mObserver != null) {
            mObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (mObserver != null) {
            mObserver.onNext(t);
        }
        if (mOnNext != null) {
            mOnNext.call(t);
        }
    }

    public static <T> Callback<T> inclusion(Action1<T> action1) {
        return new Inclusions<>(action1);
    }

    private static final class Inclusions<T> extends Callback<T> {

        Inclusions(@Nullable Action1<T> onNext) {
            super(onNext);
        }
    }
}