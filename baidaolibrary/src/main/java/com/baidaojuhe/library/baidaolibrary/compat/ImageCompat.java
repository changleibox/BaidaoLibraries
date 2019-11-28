/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;
import net.box.app.library.util.IIntentCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Box on 16/9/19.
 * <p>
 * 查看图片
 */
@SuppressWarnings("WeakerAccess")
public class ImageCompat {

    public static void openImages(IContext context, View v, int currentPosition, List<String> paths) {
        openImages(context, v, currentPosition, paths.toArray(new String[0]));
    }

    public static void openImages(IContext context, View v, int currentPosition, String... paths) {
        if (paths == null || paths.length == 0) {
            throw new IllegalStateException("图片路径不为空！");
        }
        Activity activity = context.getActivity();
        ActivityOptionsCompat optionsCompat = null;
        String transitionName;
        if (v != null && (transitionName = ViewCompat.getTransitionName(v)) != null) {
            ViewCompat.setTransitionName(v, BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + currentPosition);
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, transitionName);
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

    public static void insertImage(Context context, File file) {
        final String absolutePath = file.getAbsolutePath();
        final String fileName = file.getName();
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), absolutePath, fileName, absolutePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        // 最后通知图库更新
        final Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
    }

}
