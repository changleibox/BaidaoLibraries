/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2017/5/2.
 * <p>
 * Presenter
 */

@SuppressWarnings("WeakerAccess")
public abstract class BasePresenter<IContext extends ContextExtend> {

    private IContext mActivity;

    public BasePresenter(@NonNull IContext activity) {
        this.mActivity = activity;
    }

    public IContext getActivity() {
        return mActivity;
    }

    public Bundle getBundle() {
        return mActivity.getBundle();
    }

    public void finish() {
        mActivity.finish();
    }

    public CompositeSubscription getCompositeSubscription() {
        return mActivity.getCompositeSubscription();
    }

    public void onDestroy() {
    }

}
