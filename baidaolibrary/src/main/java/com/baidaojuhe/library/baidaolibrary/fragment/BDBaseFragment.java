/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.compat.ProgressCompat;
import com.baidaojuhe.library.baidaolibrary.impl.ActivityListener;
import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;
import com.baidaojuhe.library.baidaolibrary.impl.IContext;
import com.baidaojuhe.library.baidaolibrary.presenter.BaseClassPresenter;
import com.baidaojuhe.library.baidaolibrary.rxandroid.RxFragment;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;

import net.box.app.library.helper.IAppHelper;

import butterknife.ButterKnife;
import me.box.retrofit.impl.RetrofitProgressImpl;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2017/11/22.
 * <p>
 * fragment基类
 */

public abstract class BDBaseFragment extends RxFragment implements ContextExtend, IContext {

    private CompositeSubscription mCompositeSubscription;
    private CharSequence mTitle;

    private BaseClassPresenter mClassPresenter;

    public BDBaseFragment() {
        mClassPresenter = new BaseClassPresenter(this);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final BDLayout layout = getContainerLayout(getBundle(), savedInstanceState);
        if (layout != null) {
            return layout.createContentView(inflater, container);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public final void initViews(Bundle savedInstanceState) {
        onInitViews(getBundle(), savedInstanceState);
    }

    @Override
    public final void initDatas(Bundle savedInstanceState) {
        onInitDatas(getBundle(), savedInstanceState);
    }

    @Override
    public final void initListeners(Bundle savedInstanceState) {
        onInitListeners(getBundle(), savedInstanceState);
    }

    @Override
    public final Object getContainerLayout() {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        View view = getView();
        if (view != null) {
            ButterKnife.bind(this, view);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public BDBaseFragment setTitle(@StringRes int title) {
        return setTitle(IAppHelper.getString(title));
    }

    public BDBaseFragment setTitle(CharSequence title) {
        this.mTitle = title;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setTitle(title);
        }
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mClassPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        mClassPresenter.onActivityReenter(resultCode, data);
    }

    @Override
    public void addActivityListener(ActivityListener listener) {
        mClassPresenter.addActivityListener(listener);
    }

    @Override
    public void removeActivityListener(ActivityListener listener) {
        mClassPresenter.removeActivityListener(listener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mClassPresenter.onCreateOptionsMenu(menu);
    }

    public LayoutInflater getInflater() {
        return LayoutInflater.from(getContext());
    }

    @Override
    public RetrofitProgressImpl showRetrofitLoad() {
        return ProgressCompat.showLoad(getActivity());
    }

    @Override
    public void loadDismiss() {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            ProgressCompat.loadDismiss(activity);
        }
    }
}
