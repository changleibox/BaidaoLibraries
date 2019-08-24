/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by box on 2019/2/22.
 */
public interface LoadPromptImpl {

    int MODE_SUCCESS = -1;
    int MODE_NO_DATA = 0;
    int MODE_NO_NETWORK = 1;
    int MODE_LOAD_FAILURE = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_SUCCESS, MODE_NO_DATA, MODE_NO_NETWORK, MODE_LOAD_FAILURE})
    @interface Mode {
    }

    void setPromptText(CharSequence text);

    void setPromptText(@StringRes int text);

    void setPromptDrawable(Drawable drawable);

    void setPromptDrawable(@DrawableRes int drawable);

    void setMode(@Mode int mode);

    void setSuccess();

    void setError(Throwable e, boolean isFirstPage);
}
