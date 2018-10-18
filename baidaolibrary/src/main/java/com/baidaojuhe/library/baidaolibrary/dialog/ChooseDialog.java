/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 选择对话框
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ChooseDialog extends BDBottomSheetDialog {

    private TextView mTvTitle;
    @Nullable
    private Button mBtnConfirm;
    @Nullable
    private Button mBtnCancel;

    private FrameLayout mFlContent;
    private View mContentView;

    @SuppressLint("InflateParams")
    public ChooseDialog(Context context) {
        super(context);
        ViewGroup contentParent = findViewById(android.R.id.content);
        mContentView = getLayoutInflater().inflate(R.layout.bd_dialog_choose, contentParent, false);
        mFlContent = mContentView.findViewById(R.id.bd_fl_content);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mFlContent.removeAllViews();
        getLayoutInflater().inflate(layoutResID, mFlContent);
        super.setContentView(mContentView);

        mTvTitle = findViewById(R.id.bd_tv_title);
        mBtnConfirm = findViewById(R.id.bd_btn_confirm);
        mBtnCancel = findViewById(R.id.bd_btn_cancel);

        if (mBtnConfirm != null) {
            mBtnConfirm.setOnClickListener(this::onConfirm);
        }
        if (mBtnCancel != null) {
            mBtnCancel.setOnClickListener(this::onCancel);
        }
    }

    @Override
    public void setContentView(View view) {
        mFlContent.removeAllViews();
        mFlContent.addView(view);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFlContent.removeAllViews();
        mFlContent.addView(view, params);
        super.setContentView(mContentView, params);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        if (mBtnConfirm != null) {
            mBtnConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        setTitle(getContext().getText(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTvTitle.setText(title);
    }

    public void onCancel(View v) {
        dismiss();
    }

    public void onConfirm(View v) {
        dismiss();
    }

    public void setConfirmHiden() {
        if (mBtnConfirm != null) {
            mBtnConfirm.setVisibility(View.GONE);
        }
        if (mBtnCancel != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBtnCancel.getLayoutParams();
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
            mBtnCancel.setLayoutParams(params);
        }
    }

    public void setConfirmVisibility(@Visibility int visibility) {
        if (mBtnConfirm != null) {
            mBtnConfirm.setVisibility(visibility);
        }
    }

    public void setCancelVisibility(@Visibility int visibility) {
        if (mBtnCancel != null) {
            mBtnCancel.setVisibility(visibility);
        }
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}
}
