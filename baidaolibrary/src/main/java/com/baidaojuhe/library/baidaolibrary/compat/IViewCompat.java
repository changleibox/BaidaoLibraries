/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by box on 2017/12/12.
 * <p>
 * View的兼容类
 */

public class IViewCompat {

    public static void addOnceOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                listener.onGlobalLayout();
            }
        });
    }

    public static <T extends View> T findById(@NonNull Dialog dialog, @IdRes int id) {
        return dialog.findViewById(id);
    }

    public static <T extends View> T findById(@NonNull View view, @IdRes int id) {
        return view.findViewById(id);
    }

    public static <T extends View> T findById(@NonNull Activity activity, @IdRes int id) {
        return activity.findViewById(id);
    }

}
