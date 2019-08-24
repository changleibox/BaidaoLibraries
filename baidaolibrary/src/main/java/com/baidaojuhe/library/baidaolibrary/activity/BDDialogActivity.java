/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.compat.RingtoneCompat;

import net.box.app.library.IContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by box on 2017/5/25.
 * <p>
 * dialog模式的Activity
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class BDDialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KET_BUILDER = "builder";

    private TextView mTvTile;
    private TextView mTvMessage;
    private Button mBtnPositive;
    private Button mBtnNegative;

    private Builder mBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(mBuilder = getIntent().getParcelableExtra(KET_BUILDER));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bd_btn_negative) {
            EventBus.getDefault().post(Which.Negative);
        } else if (i == R.id.bd_btn_positive) {
            EventBus.getDefault().post(Which.Positive);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!mBuilder.mCancelable) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        EventBus.getDefault().post(Which.Dismiss);
        super.finish();
    }

    private void initContentLayout() {
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

    private void create(final Builder builder) {
        initContentLayout();
        mTvTile.setText(builder.mTitle);
        mTvTile.setVisibility(TextUtils.isEmpty(builder.mTitle) ? View.GONE : View.VISIBLE);
        mTvMessage.setText(builder.mMessage);
        if (TextUtils.isEmpty(builder.mNegativeButtonText)) {
            mBtnNegative.setVisibility(View.GONE);
            mBtnPositive.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom);
        }
        if (TextUtils.isEmpty(builder.mPositiveButtonText)) {
            mBtnPositive.setVisibility(View.GONE);
            mBtnNegative.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom);
        }
        if (!TextUtils.isEmpty(builder.mPositiveButtonText) && !TextUtils.isEmpty(builder.mNegativeButtonText)) {
            mBtnNegative.setVisibility(View.VISIBLE);
            mBtnPositive.setVisibility(View.VISIBLE);
            mBtnNegative.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom_left);
            mBtnPositive.setBackgroundResource(R.drawable.bd_selector_radius_button_bottom_right);
        }
        if (builder.mNegativeButtonTextColor != null) {
            mBtnNegative.setTextColor(builder.mNegativeButtonTextColor);
        }
        if (builder.mPositiveButtonTextColor != null) {
            mBtnPositive.setTextColor(builder.mPositiveButtonTextColor);
        }
        mBtnNegative.setText(builder.mNegativeButtonText);
        mBtnPositive.setText(builder.mPositiveButtonText);

        mBtnNegative.setOnClickListener(this);
        mBtnPositive.setOnClickListener(this);

        setFinishOnTouchOutside(builder.mCanceledOnTouchOutside);

        if (builder.isPlayRanging) {
            RingtoneCompat.play(this);
        }
    }

    public interface OnClickListener {
        void onClick(Builder builder, int which);
    }

    public interface OnDismissListener {
        void onDismiss(Builder builder);
    }

    public abstract static class OnButtonClick implements OnClickListener {

        @Override
        public void onClick(Builder builder, int which) {
        }
    }

    public abstract static class OnWindowDismiss implements OnDismissListener {

        @Override
        public void onDismiss(Builder builder) {
        }
    }

    public static class Builder implements Parcelable {

        transient private Context mContext;
        private String mTitle;
        private String mMessage;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private boolean isPlayRanging;
        transient private BDDialogActivity.OnClickListener mPositiveButtonListener;
        transient private BDDialogActivity.OnClickListener mNegativeButtonListener;
        transient private BDDialogActivity.OnDismissListener mOnDismissListener;
        private ColorStateList mPositiveButtonTextColor;
        private ColorStateList mNegativeButtonTextColor;

        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        public Builder(@NonNull IContext context) {
            this.mContext = context.getActivity();
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(@StringRes int titleId) {
            return setTitle(mContext.getString(titleId));
        }

        /**
         * Set the title displayed in the {@link android.app.Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(@Nullable String title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            mMessage = mContext.getString(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(@Nullable CharSequence message) {
            mMessage = message == null ? null : message.toString();
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setPositiveButton(null, textId, listener);
        }

        public Builder setPositiveButton(String text, final BDDialogActivity.OnClickListener listener) {
            return setPositiveButton(null, text, listener);
        }

        public Builder setPositiveButton(@ColorInt int color, @StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setPositiveButton(ColorStateList.valueOf(color), textId, listener);
        }

        public Builder setPositiveButton(@ColorInt int color, String text, final BDDialogActivity.OnClickListener listener) {
            return setPositiveButton(ColorStateList.valueOf(color), text, listener);
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link android.content.DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(ColorStateList color, @StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setPositiveButton(color, mContext.getString(textId), listener);
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link android.content.DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(ColorStateList color, String text, final BDDialogActivity.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            mPositiveButtonTextColor = color;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setNegativeButton(null, textId, listener);
        }

        public Builder setNegativeButton(String text, final BDDialogActivity.OnClickListener listener) {
            return setNegativeButton(null, text, listener);
        }

        public Builder setNegativeButton(@ColorInt int color, @StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setNegativeButton(ColorStateList.valueOf(color), textId, listener);
        }

        public Builder setNegativeButton(@ColorInt int color, String text, final BDDialogActivity.OnClickListener listener) {
            return setNegativeButton(ColorStateList.valueOf(color), text, listener);
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link android.content.DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(ColorStateList color, @StringRes int textId, final BDDialogActivity.OnClickListener listener) {
            return setNegativeButton(color, mContext.getString(textId), listener);
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link android.content.DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(ColorStateList color, String text, final BDDialogActivity.OnClickListener listener) {
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
            if (cancel && !mCancelable) {
                mCancelable = true;
            }
            mCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder setPlayRanging(boolean playRanging) {
            isPlayRanging = playRanging;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(BDDialogActivity.OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Creates an {@link .dialog.IAlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     IAlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public Builder show() {
            EventBus.getDefault().register(this);
            Intent intent = new Intent(mContext, BDDialogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(KET_BUILDER, this);
            mContext.startActivity(intent);
            return this;
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onClickButtonEvent(Which which) {
            EventBus.getDefault().unregister(this);
            switch (which) {
                case Positive:
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(this, BUTTON_POSITIVE);
                    }
                    break;
                case Negative:
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(this, BUTTON_NEGATIVE);
                    }
                    break;
                case Dismiss:
                    if (mOnDismissListener != null) {
                        mOnDismissListener.onDismiss(this);
                    }
                    break;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mTitle);
            dest.writeString(this.mMessage);
            dest.writeString(this.mPositiveButtonText);
            dest.writeString(this.mNegativeButtonText);
            dest.writeByte(this.mCancelable ? (byte) 1 : (byte) 0);
            dest.writeByte(this.mCanceledOnTouchOutside ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isPlayRanging ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.mPositiveButtonTextColor, flags);
            dest.writeParcelable(this.mNegativeButtonTextColor, flags);
        }

        protected Builder(Parcel in) {
            this.mTitle = in.readString();
            this.mMessage = in.readString();
            this.mPositiveButtonText = in.readString();
            this.mNegativeButtonText = in.readString();
            this.mCancelable = in.readByte() != 0;
            this.mCanceledOnTouchOutside = in.readByte() != 0;
            this.isPlayRanging = in.readByte() != 0;
            this.mPositiveButtonTextColor = in.readParcelable(ColorStateList.class.getClassLoader());
            this.mNegativeButtonTextColor = in.readParcelable(ColorStateList.class.getClassLoader());
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
    }

    private enum Which {
        Positive, Negative, Dismiss
    }
}
