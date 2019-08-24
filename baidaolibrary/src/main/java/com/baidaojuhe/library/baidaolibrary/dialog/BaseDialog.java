/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import androidx.appcompat.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;

import butterknife.ButterKnife;

/**
 * Created by box on 2017/4/18.
 * <p>
 * 对话框基类
 */

public abstract class BaseDialog extends AppCompatDialog {

    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void onContentChanged() {
        ButterKnife.bind(this);
    }

    private void init() {
        final Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
    }
}
