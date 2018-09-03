/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.baidaojuhe.library.baidaolibrary.R;

/**
 * Created by box on 2017/4/18.
 * <p>
 * 有背景色popupwindow
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HasBackgroundPopupWindow extends SupportPopupWindow implements
        PopupWindow.OnDismissListener {

    public static final int BACKGROUND = 0x55000000;

    private PopupWindow mBackground;
    private PopupWindow.OnDismissListener mListener;

    public HasBackgroundPopupWindow(Context context) {
        super(context);
        initBackground(context);
    }

    public HasBackgroundPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackground(context);
    }

    public HasBackgroundPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground(context);
    }

    public HasBackgroundPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBackground(context);
    }

    public HasBackgroundPopupWindow(View contentView) {
        super(contentView);
        initBackground(contentView.getContext());
    }

    public HasBackgroundPopupWindow(int width, int height) {
        super(width, height);
        initBackground(null);
    }

    public HasBackgroundPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        initBackground(contentView.getContext());
    }

    public HasBackgroundPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        initBackground(contentView.getContext());
    }

    private void initBackground(@Nullable Context context) {
        mBackground = new SupportPopupWindow(context);
        if (context != null) {
            View emptyView = new View(context);
            emptyView.setBackgroundColor(BACKGROUND);
            mBackground.setContentView(emptyView);
        }
        mBackground.setAnimationStyle(R.style.BD_AppTheme_Anim_PopupWindow_Background);
        mBackground.setWidth(LayoutParams.MATCH_PARENT);
        mBackground.setHeight(LayoutParams.MATCH_PARENT);
        mBackground.setBackgroundDrawable(new ColorDrawable(BACKGROUND));
        mBackground.setOutsideTouchable(true);
        mBackground.setFocusable(true);
        PopupWindowCompat.setOverlapAnchor(mBackground, false);

        super.setOnDismissListener(this);
        mBackground.setOnDismissListener(() -> {
            if (!isShowing()) {
                return;
            }
            dismiss();
        });
    }

    @Override
    public void setSoftInputMode(int mode) {
        mBackground.setSoftInputMode(mode);
        super.setSoftInputMode(mode);
    }

    @Override
    public void setInputMethodMode(int mode) {
        mBackground.setInputMethodMode(mode);
        super.setInputMethodMode(mode);
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        mBackground.setOutsideTouchable(touchable);
        super.setOutsideTouchable(touchable);
    }

    @Override
    public void setFocusable(boolean focusable) {
        mBackground.setFocusable(focusable);
        super.setFocusable(focusable);
    }

    @Override
    public void setTouchable(boolean touchable) {
        mBackground.setTouchable(touchable);
        super.setTouchable(touchable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        boolean intercept = onShowBefore();
        if (intercept) {
            return;
        }
        PopupWindowCompat.showAsDropDown(mBackground, anchor, 0, 0, Gravity.NO_GRAVITY);
        super.showAsDropDown(anchor);
        onShowAfter();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        boolean intercept = onShowBefore();
        if (intercept) {
            return;
        }
        PopupWindowCompat.showAsDropDown(mBackground, anchor, 0, yoff, Gravity.NO_GRAVITY);
        super.showAsDropDown(anchor, xoff, yoff);
        onShowAfter();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        boolean intercept = onShowBefore();
        if (intercept) {
            return;
        }
        PopupWindowCompat.showAsDropDown(mBackground, anchor, 0, yoff, Gravity.NO_GRAVITY);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        onShowAfter();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        boolean intercept = onShowBefore();
        if (intercept) {
            return;
        }
        mBackground.showAtLocation(parent, Gravity.CENTER, 0, 0);
        super.showAtLocation(parent, gravity, x, y);
        onShowAfter();
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDismiss() {
        if (mBackground != null) {
            mBackground.dismiss();
        }
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    @CheckResult
    protected boolean onShowBefore() {
        return false;
    }

    protected void onShowAfter() {
    }

}
