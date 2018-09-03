/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import butterknife.ButterKnife;

/**
 * Created by box on 2017/4/18.
 * <p>
 * 对话框基类
 */

public abstract class BaseDialog extends AppCompatDialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onContentChanged() {
        ButterKnife.bind(this);
    }
}
