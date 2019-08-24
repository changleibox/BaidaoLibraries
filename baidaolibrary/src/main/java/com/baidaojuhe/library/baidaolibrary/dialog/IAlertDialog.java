/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import net.box.app.library.IContext;

/**
 * Created by box on 2017/3/21.
 * <p>
 * 全局的提示对话框
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class IAlertDialog extends BaseDialog {

    private TextView mTvTile;
    private TextView mTvMessage;
    private Button mBtnPositive;
    private Button mBtnNegative;

    protected IAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected IAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        init();
    }

    protected IAlertDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.bd_dialog_alert);

        mTvTile = IViewCompat.findById(this, R.id.bd_tv_title);
        mTvMessage = IViewCompat.findById(this, R.id.bd_tv_message);
        mBtnPositive = IViewCompat.findById(this, R.id.bd_btn_positive);
        mBtnNegative = IViewCompat.findById(this, R.id.bd_btn_negative);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            DisplayMetrics dm = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(dm);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            // lp.width = dm.widthPixels - getContext().getResources().getDimensionPixelSize(R.dimen.sizeDialogMargin);
            window.setAttributes(lp);
        }
    }

    private static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
        if (resid >= 0x01000000) {   // start of real resource IDs.
            return resid;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    public static class Builder {

        private final Context mContext;
        private final int mTheme;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private DialogInterface.OnClickListener mPositiveButtonListener;
        private DialogInterface.OnClickListener mNegativeButtonListener;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private DialogInterface.OnCancelListener mOnCancelListener;
        private DialogInterface.OnDismissListener mOnDismissListener;
        private DialogInterface.OnKeyListener mOnKeyListener;
        private ColorStateList mPositiveButtonTextColor;
        private ColorStateList mNegativeButtonTextColor;

        public Builder(@NonNull Context context) {
            this(context, resolveDialogTheme(context, R.style.BD_AppTheme_Dialog_Translucent));
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            mTheme = themeResId;
            mContext = context;
        }

        public Builder(@NonNull IContext context) {
            this(context.getActivity());
        }

        public Builder(@NonNull IContext context, @StyleRes int themeResId) {
            this(context.getActivity(), themeResId);
        }

        public Context getContext() {
            return mContext;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(@StringRes int titleId) {
            return setTitle(mContext.getText(titleId));
        }

        /**
         * Set the title displayed in the {@link android.app.Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(@Nullable CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            mMessage = mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(@Nullable CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setPositiveButton(null, textId, listener);
        }

        public Builder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            return setPositiveButton(null, text, listener);
        }

        public Builder setPositiveButton(@ColorInt int color, @StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setPositiveButton(ColorStateList.valueOf(color), textId, listener);
        }

        public Builder setPositiveButton(@ColorInt int color, CharSequence text, final DialogInterface.OnClickListener listener) {
            return setPositiveButton(ColorStateList.valueOf(color), text, listener);
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(ColorStateList color, @StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setPositiveButton(color, mContext.getText(textId), listener);
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(ColorStateList color, CharSequence text, final DialogInterface.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            mPositiveButtonTextColor = color;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setNegativeButton(null, textId, listener);
        }

        public Builder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            return setNegativeButton(null, text, listener);
        }

        public Builder setNegativeButton(@ColorInt int color, @StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setNegativeButton(ColorStateList.valueOf(color), textId, listener);
        }

        public Builder setNegativeButton(@ColorInt int color, CharSequence text, final DialogInterface.OnClickListener listener) {
            return setNegativeButton(ColorStateList.valueOf(color), text, listener);
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(ColorStateList color, @StringRes int textId, final DialogInterface.OnClickListener listener) {
            return setNegativeButton(color, mContext.getText(textId), listener);
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(ColorStateList color, CharSequence text, final DialogInterface.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            mNegativeButtonTextColor = color;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            mCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            mOnKeyListener = onKeyListener;
            return this;
        }

        public IAlertDialog create() {
            // We can'object use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final IAlertDialog dialog = new IAlertDialog(mContext, mTheme);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            dialog.mTvTile.setText(mTitle);
            dialog.mTvTile.setVisibility(TextUtils.isEmpty(mTitle) ? View.GONE : View.VISIBLE);
            dialog.mTvMessage.setText(mMessage);
            if (TextUtils.isEmpty(mNegativeButtonText)) {
                dialog.mBtnNegative.setVisibility(View.GONE);
                dialog.mBtnPositive.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom);
            }
            if (TextUtils.isEmpty(mPositiveButtonText)) {
                dialog.mBtnPositive.setVisibility(View.GONE);
                dialog.mBtnNegative.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom);
            }
            if (!TextUtils.isEmpty(mPositiveButtonText) && !TextUtils.isEmpty(mNegativeButtonText)) {
                dialog.mBtnNegative.setVisibility(View.VISIBLE);
                dialog.mBtnPositive.setVisibility(View.VISIBLE);
                dialog.mBtnNegative.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom_left);
                dialog.mBtnPositive.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom_right);
            }
            if (mNegativeButtonTextColor != null) {
                dialog.mBtnNegative.setTextColor(mNegativeButtonTextColor);
            }
            if (mPositiveButtonTextColor != null) {
                dialog.mBtnPositive.setTextColor(mPositiveButtonTextColor);
            }
            dialog.mBtnNegative.setText(mNegativeButtonText);
            dialog.mBtnPositive.setText(mPositiveButtonText);
            View.OnClickListener clickListener = v -> {
                int i = v.getId();
                if (i == R.id.bd_btn_negative) {
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(dialog, BUTTON_NEGATIVE);
                    }
                } else if (i == R.id.bd_btn_positive) {
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(dialog, BUTTON_POSITIVE);
                    }
                }
                dialog.dismiss();
            };
            dialog.mBtnNegative.setOnClickListener(clickListener);
            dialog.mBtnPositive.setOnClickListener(clickListener);
            if (mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(mOnCancelListener);
            dialog.setOnDismissListener(mOnDismissListener);
            if (mOnKeyListener != null) {
                dialog.setOnKeyListener(mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link IAlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     IAlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public IAlertDialog show() {
            final IAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}
