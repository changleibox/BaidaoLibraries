/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.activity.BDSearchActivity;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;
import net.box.app.library.util.IIntentCompat;

/**
 * Created by box on 2017/12/7.
 * <p>
 * app
 */

@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
public class BDAppCompat {

    public static <Activity extends BDSearchActivity> void search(@NonNull IContext context, @NonNull Class<Activity> cls, @Nullable Bundle bundle, @Nullable View view) {
        android.app.Activity activity = context.getActivity();
        ActivityOptionsCompat optionsCompat = null;
        if (view != null) {
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, BDConstants.BDTransitionName.TRANSITION_NAME_SEARCH);
        }
        Intent intent = new Intent(context.getContext(), cls);
        if (optionsCompat != null) {
            IIntentCompat.startActivity(activity, intent, bundle, optionsCompat.toBundle());
        } else {
            context.startActivity(intent, bundle);
        }
    }
}
