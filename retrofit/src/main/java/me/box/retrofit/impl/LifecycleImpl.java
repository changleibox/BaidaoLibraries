/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.impl;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2018/5/28.
 * <p>
 * 生命周期
 */
public interface LifecycleImpl {

    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindToLifecycle();

    CompositeSubscription getCompositeSubscription();
}
