/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.app.Activity;
import android.content.Context;

import net.box.app.library.widget.IProgress;

import me.box.retrofit.impl.RetrofitProgressImpl;

/**
 * Created by box on 2018/5/25.
 * <p>
 * 进度对话框
 */
public class Progress extends IProgress implements RetrofitProgressImpl {

    public Progress(Context context) {
        super(context);
    }

    public Progress(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        try {
            Activity activity = null;
            if (getContext() instanceof Activity) {
                activity = (Activity) getContext();
            }
            if (!isShowing() && (activity == null || (!activity.isFinishing() && !activity.isDestroyed()))) {
                super.show();
            }
        } catch (Exception ignored) {
        }
    }
}
