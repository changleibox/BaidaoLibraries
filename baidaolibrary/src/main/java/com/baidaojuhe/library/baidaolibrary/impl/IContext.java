/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.util.BDLayout;

import me.box.retrofit.impl.RetrofitContext;

/**
 * Created by box on 2018/8/28.
 */
public interface IContext extends net.box.app.library.IContext, RetrofitContext {

    /**
     * 初始化View
     */
    void onInitViews(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void onInitDatas(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState);

    /**
     * 初始化数据监听
     */
    void onInitListeners(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState);

    BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState);
}
