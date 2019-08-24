/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.presenter;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.Menu;

import com.baidaojuhe.library.baidaolibrary.helper.BaseClassHelper;
import com.baidaojuhe.library.baidaolibrary.impl.ActivityListener;
import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;
import com.baidaojuhe.library.baidaolibrary.impl.ContextImpl;

/**
 * Created by box on 2017/8/18.
 * <p>
 * 处理基类
 */

public class BaseClassPresenter extends BasePresenter<ContextExtend> implements ContextImpl {

    public BaseClassPresenter(@NonNull ContextExtend activity) {
        super(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseClassHelper.onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        BaseClassHelper.onActivityReenter(getActivity(), resultCode, data);
    }

    @Override
    public final void addActivityListener(ActivityListener listener) {
        BaseClassHelper.addActivityListener(getActivity(), listener);
    }

    @Override
    public final void removeActivityListener(ActivityListener listener) {
        BaseClassHelper.removeActivityListener(getActivity(), listener);
    }

    public void onCreateOptionsMenu(Menu menu) {
        // IViewCompat.onCreateOptionsMenu(getActivity().getActivity(), menu);
    }
}
