/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * Created by box on 2017/6/20.
 * <p>
 * PopupWindow
 */

@SuppressWarnings({"WeakerAccess", "unused"})
@SuppressLint("PrivateApi")
public class SupportPopupWindow extends PopupWindow {

    private static final String TAG = SupportPopupWindow.class.getSimpleName();

    private static Method sClipToWindowEnabledMethod;

    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
    }

    public SupportPopupWindow(Context context) {
        super(context);
    }

    public SupportPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SupportPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SupportPopupWindow() {
        super();
    }

    public SupportPopupWindow(View contentView) {
        super(contentView);
    }

    public SupportPopupWindow(int width, int height) {
        super(width, height);
    }

    public SupportPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public SupportPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        refreshBackgroundHeight(anchor, 0);
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        refreshBackgroundHeight(anchor, yoff);
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        refreshBackgroundHeight(anchor, yoff);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public final void setClipToScreenEnabled(boolean clip) {
        if (sClipToWindowEnabledMethod != null) {
            try {
                sClipToWindowEnabledMethod.invoke(this, clip);
            } catch (Exception e) {
                Log.i(TAG, "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }

    public void refreshBackgroundHeight(View anchor, int yoff) {
        if (getHeight() == LayoutParams.WRAP_CONTENT) {
            return;
        }
        setHeightToMatchParent(anchor, yoff);
    }

    public void setHeightToMatchParent(View anchor, int yoff) {
        Rect rect = new Rect();
        anchor.getGlobalVisibleRect(rect);
        setHeight(anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom - yoff);
        update();
    }
}
