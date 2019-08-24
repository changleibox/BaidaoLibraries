/*
 * Copyright © 2018 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.baidaojuhe.library.baidaolibrary.widget.Progress;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by box on 2018/8/28.
 * <p>
 * 进度条
 */
public class ProgressCompat {

    private static final Map<Context, Progress> PROGRESS_MAP = Collections.synchronizedMap(new HashMap<Context, Progress>());

    private static Progress.Theme mTheme = Progress.Theme.Black;

    public static Progress showLoad(@NonNull Context context, CharSequence message) {
        Progress progress = null;
        try {
            progress = PROGRESS_MAP.get(context);
            if (progress == null) {
                progress = new Progress(context);
                progress.setCancelable(true);
                progress.setCanceledOnTouchOutside(false);
            }
            progress.setTheme(mTheme);
            progress.setMessage(message);
            PROGRESS_MAP.put(context, progress);
        } catch (Exception ignored) {
        }
        return progress;
    }

    public static Progress showLoad(Context context, @StringRes int messageResId) {
        return showLoad(context, messageResId == -1 ? null : context.getString(messageResId));
    }

    public static Progress showLoad(Context context) {
        return showLoad(context, -1);
    }

    public static Progress showLoad(Context context, Progress.Theme theme, CharSequence message) {
        Progress progress = showLoad(context, message);
        if (progress != null) {
            progress.setTheme(theme);
        }
        return progress;
    }

    public static Progress showLoad(Context context, Progress.Theme theme, @StringRes int messageResId) {
        return showLoad(context, theme, messageResId == -1 ? null : context.getString(messageResId));
    }

    public static Progress showLoad(Context context, Progress.Theme theme) {
        return showLoad(context, theme, -1);
    }

    public static void loadDismiss(@NonNull Context context) {
        try {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
                    return;
                }
            }
            Progress progress = PROGRESS_MAP.get(context);
            if (progress != null) {
                progress.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    public static void onContextDestroy(@NonNull Context context) {
        loadDismiss(context);
        PROGRESS_MAP.remove(context);
    }

    /**
     * 请在第一次显示之前调用，否则无效
     *
     * @param theme {@link Progress} 默认为黑色主题
     */
    public static void setTheme(Progress.Theme theme) {
        ProgressCompat.mTheme = theme;
    }
}
