/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 选择对话框
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ChooseDialog extends BDBottomSheetDialog {

    private final TextView mTvTitle;
    final Button mBtnConfirm;
    final Button mBtnCancel;

    private FrameLayout mFlContent;
    private View mContentView;

    @SuppressLint("InflateParams")
    public ChooseDialog(Context context) {
        super(context);
        ViewGroup contentParent = findViewById(android.R.id.content);
        mContentView = getLayoutInflater().inflate(R.layout.bd_dialog_choose, contentParent, false);
        mFlContent = IViewCompat.findById(mContentView, R.id.bd_fl_content);
        mTvTitle = IViewCompat.findById(mContentView, R.id.bd_tv_title);
        mBtnConfirm = IViewCompat.findById(mContentView, R.id.bd_btn_confirm);
        mBtnCancel = IViewCompat.findById(mContentView, R.id.bd_btn_cancel);

        mBtnConfirm.setOnClickListener(this::onConfirm);
        mBtnCancel.setOnClickListener(this::onCancel);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mFlContent.removeAllViews();
        getLayoutInflater().inflate(layoutResID, mFlContent);
        super.setContentView(mContentView);
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

        setConfirmVisiable(View.GONE);
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

    public void setConfirmVisiable(@Visibility int visibility) {
        if (mBtnConfirm != null) {
            mBtnConfirm.setVisibility(visibility);
        }
    }

    public void setCancelVisiable(@Visibility int visibility) {
        if (mBtnCancel != null) {
            mBtnCancel.setVisibility(visibility);
        }
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}
}
