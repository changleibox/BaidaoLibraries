/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;

import com.baidaojuhe.library.baidaolibrary.compat.ProgressCompat;
import com.baidaojuhe.library.baidaolibrary.impl.ActivityListener;
import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;
import com.baidaojuhe.library.baidaolibrary.impl.IContext;
import com.baidaojuhe.library.baidaolibrary.presenter.BaseClassPresenter;
import com.baidaojuhe.library.baidaolibrary.rxandroid.RxAppCompatActivity;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;

import butterknife.ButterKnife;
import me.box.retrofit.impl.RetrofitProgressImpl;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by box on 2017/11/22.
 * <p>
 * activity基类
 */

public abstract class BDBaseActivity extends RxAppCompatActivity implements ContextExtend, IContext {

    private CompositeSubscription mCompositeSubscription;
    private BaseClassPresenter mClassPresenter;

    public BDBaseActivity() {
        mClassPresenter = new BaseClassPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BDLayout containerLayout = getContainerLayout(getBundle(), savedInstanceState);
        if (containerLayout != null) {
            containerLayout.setContentView(this);
        }
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
    public void onContentChanged() {
        ButterKnife.bind(this);
        super.onContentChanged();
    }

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    public void onUserCreateViews(Bundle savedInstanceState) {
    }

    @Override
    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }

    @Override
    protected void onDestroy() {
        ProgressCompat.onContextDestroy(this);
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mClassPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        mClassPresenter.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public RetrofitProgressImpl showRetrofitLoad() {
        return ProgressCompat.showLoad(this);
    }

    @Override
    public void loadDismiss() {
        ProgressCompat.loadDismiss(this);
    }
}
