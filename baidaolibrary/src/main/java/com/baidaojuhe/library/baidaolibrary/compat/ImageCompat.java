/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;
import net.box.app.library.util.IIntentCompat;

import java.util.List;

/**
 * Created by Box on 16/9/19.
 * <p>
 * 查看图片
 */
@SuppressWarnings("WeakerAccess")
public class ImageCompat {

    public static void openImages(IContext context, View v, int currentPosition, List<String> paths) {
        openImages(context, v, currentPosition, paths.toArray(new String[paths.size()]));
    }

    public static void openImages(IContext context, View v, int currentPosition, String... paths) {
        if (paths == null || paths.length == 0) {
            throw new IllegalStateException("图片路径不为空！");
        }
        Activity activity = context.getActivity();
        ActivityOptionsCompat optionsCompat = null;
        if (v != null) {
            ViewCompat.setTransitionName(v, BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + currentPosition);
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, ViewCompat.getTransitionName(v));
        }
        Bundle bundle = new Bundle();
        bundle.putStringArray(BDConstants.BDKey.KEY_IMAGE_PATHS, paths);
        bundle.putInt(BDConstants.BDKey.KEY_CURRENT_POSITION, currentPosition);
        Intent intent = new Intent(BDConstants.BDIntentAction.ACTION_PREVIEW);
        if (optionsCompat == null) {
            context.startActivityForResult(intent.putExtras(bundle), BDConstants.BDRequestCode.RESUEST_CODE_PREVIEW_IMAGE);
        } else {
            IIntentCompat.startActivityForResult(activity, intent, bundle, BDConstants.BDRequestCode.RESUEST_CODE_PREVIEW_IMAGE, optionsCompat.toBundle());
        }
    }

}
