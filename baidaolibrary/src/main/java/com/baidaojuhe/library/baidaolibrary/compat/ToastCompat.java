/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IToastCompat;

/**
 * Created by box on 2017/8/8.
 * <p>
 * 显示toast
 */

public class ToastCompat {

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    private @interface Duration {
    }

    public static Object showText(CharSequence text, @Duration int duration) {
        return IToastCompat.showText(IAppHelper.getContext(), text, duration);
    }

    public static Object showText(CharSequence text) {
        return IToastCompat.showText(IAppHelper.getContext(), text);
    }

    public static Object showText(@StringRes int res) {
        return IToastCompat.showText(IAppHelper.getContext(), res);
    }

    public static Object showText(@StringRes int res, Object... formatArgs) {
        return IToastCompat.showText(IAppHelper.getContext(), res, formatArgs);
    }
}
