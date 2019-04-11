/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("WeakerAccess")
public class BDLayout {

    @Nullable
    private final Object mLayout;
    @Nullable
    private final ViewGroup.LayoutParams mLayoutParams;

    protected BDLayout(@Nullable Object layout, @Nullable ViewGroup.LayoutParams layoutParams) {
        this.mLayout = layout;
        this.mLayoutParams = layoutParams;
    }

    public void setContentView(@NonNull Activity activity) {
        View contentView = createContentView(activity);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }

    @Nullable
    public View createContentView(@NonNull Activity activity) {
        return createContentView(activity.getLayoutInflater(), (ViewGroup) activity.getWindow().getDecorView());
    }

    @Nullable
    public View createContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        if (mLayout == null) {
            return null;
        }
        if (mLayout instanceof Integer) {
            return inflater.inflate((Integer) mLayout, container, false);
        } else if (mLayout instanceof View) {
            final View contentView = (View) this.mLayout;
            if (mLayoutParams == null) {
                return contentView;
            } else {
                contentView.setLayoutParams(mLayoutParams);
                return contentView;
            }
        }
        return null;
    }

    public static BDLayout create(@LayoutRes int layoutResId) {
        return new BDLayout(layoutResId, null);
    }

    public static BDLayout create(@NonNull View contentView) {
        return new BDLayout(contentView, null);
    }

    public static BDLayout create(@NonNull View contentView, ViewGroup.LayoutParams layoutParams) {
        return new BDLayout(contentView, layoutParams);
    }
}