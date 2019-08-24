/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.helper.IAppHelper;

import butterknife.ButterKnife;

/**
 * Created by box on 2017/3/27.
 * <p>
 * ViewHolder基类
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    private Context mContext;

    public BaseViewHolder(@LayoutRes int id, @NonNull ViewGroup parent) {
        this(parent.getContext(), inflate(id, parent));
    }

    public BaseViewHolder(@NonNull View itemView) {
        this(itemView.getContext(), itemView);
    }

    public BaseViewHolder(@NonNull Context context, @NonNull View itemView) {
        this(context, itemView, true);
    }

    public BaseViewHolder(@NonNull Context context, @NonNull View itemView, boolean bindViews) {
        super(itemView);
        this.mContext = context;
        if (bindViews) {
            ButterKnife.bind(this, itemView);
        }
    }

    public BaseViewHolder setContext(@NonNull Context context) {
        this.mContext = context;
        return this;
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    public <T extends View> T findById(@IdRes int id) {
        return findById(itemView, id);
    }

    public static <T extends View> T findById(View view, @IdRes int id) {
        return IViewCompat.findById(view, id);
    }

    public static String getString(@StringRes int resId) {
        return IAppHelper.getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return IAppHelper.getString(resId, formatArgs);
    }

    public static int getColor(@ColorRes int resId) {
        return IAppHelper.getColor(resId);
    }

    protected static View inflate(@LayoutRes int id, @NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
    }

    public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
    }

    public void onBindDatas(int position) {
    }
}
