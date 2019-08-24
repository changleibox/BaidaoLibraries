/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.activity.BDDialogActivity;
import com.baidaojuhe.library.baidaolibrary.dialog.IAlertDialog;

/**
 * Created by Box on 17/3/14.
 * <p/>
 * 用来显示dialog
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class DialogCompat {

    public static IAlertDialog show(@NonNull Activity activity, @StringRes int message,
                                    @StringRes int positiveButton, @StringRes int negationButton, OnClickListener listener) {
        return show(activity, null, message == 0 ? null : activity.getText(message),
                positiveButton == 0 ? null : activity.getText(positiveButton), negationButton == 0 ? null : activity.getText(negationButton), listener);
    }

    public static IAlertDialog show(@NonNull Activity activity, @StringRes int title, @StringRes int message,
                                    @StringRes int positiveButton, @StringRes int negationButton, OnClickListener listener) {
        return show(activity, activity.getText(title), activity.getText(message),
                activity.getText(positiveButton), activity.getText(negationButton), listener);
    }

    public static IAlertDialog show(@NonNull Activity activity, CharSequence title, CharSequence message,
                                    CharSequence positiveButton, CharSequence negationButton, OnClickListener listener) {
        return new IAlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, listener)
                .setNegativeButton(negationButton, listener)
                .show();
    }

    public static void show(@NonNull Context context, @StringRes int message, OnClickListener listener) {
        show(context, context.getString(message), listener);
    }

    public static void show(@NonNull Context context, CharSequence message, OnClickListener listener) {
        new IAlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.box_btn_sure, listener)
                .setNegativeButton(R.string.box_btn_cancel, null)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .show();
    }

    public static void showAppDialog(@NonNull Context context, @StringRes int message, BDDialogActivity.OnClickListener listener) {
        showAppDialog(context, context.getString(message), listener);
    }

    public static void showAppDialog(@NonNull Context context, CharSequence message, BDDialogActivity.OnClickListener listener) {
        new BDDialogActivity.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.box_btn_sure, listener)
                .setNegativeButton(R.string.box_btn_cancel, null)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .show();
    }

    public static void showConfirm(@NonNull Context context, @StringRes int message, boolean isPlayRanging, boolean cancelable, BDDialogActivity.OnClickListener listener) {
        showConfirm(context, context.getString(message), isPlayRanging, cancelable, listener);
    }

    public static void showMessage(@NonNull Context context, @StringRes int message, boolean isPlayRanging, BDDialogActivity.OnClickListener listener) {
        showMessage(context, context.getString(message), isPlayRanging, listener);
    }

    public static void showConfirm(@NonNull Context context, CharSequence message, boolean isPlayRanging, boolean cancelable, BDDialogActivity.OnClickListener listener) {
        new BDDialogActivity.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.box_btn_sure, listener)
                .setCanceledOnTouchOutside(cancelable)
                .setCancelable(cancelable)
                .setPlayRanging(isPlayRanging)
                .show();
    }

    public static void showMessage(@NonNull Context context, CharSequence message, boolean isPlayRanging, BDDialogActivity.OnClickListener listener) {
        new BDDialogActivity.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.bd_btn_kown, listener)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .setPlayRanging(isPlayRanging)
                .show();
    }

}
