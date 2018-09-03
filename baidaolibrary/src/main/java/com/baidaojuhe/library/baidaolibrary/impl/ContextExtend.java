/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;

import net.box.app.library.IContext;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2017/7/25.
 * <p>
 * 上下文环境的扩展
 */

public interface ContextExtend extends IContext, ContextImpl {

    CompositeSubscription getCompositeSubscription();

    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindToLifecycle();
}
