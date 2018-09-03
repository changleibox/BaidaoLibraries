/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.ICircleTransform;

import java.io.File;

/**
 * Created by Box on 17/3/15.
 * <p/>
 * 加载头像
 */
@SuppressWarnings("unused")
public class AvatarUtils {

    private static final Transformation TRANSFORMATION = new ICircleTransform((int) IAppHelper.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4));
    private static final Transformation TRANSFORMATION_CIRCLE = new ICircleTransform(ICircleTransform.CIRCLE);

    public static void setRadiusAvatar(String url, ImageView imageView) {
        setAvatar(url, imageView, R.drawable.box_default_avatar_rect, TRANSFORMATION);
    }

    public static void setRectAvatar(String url, ImageView imageView) {
        setAvatar(url, imageView, 0, null);
    }

    public static void setCircleAvatar(String url, ImageView imageView) {
        setAvatar(url, imageView, R.drawable.box_default_avatar_cricle, TRANSFORMATION_CIRCLE);
    }

    private static void setAvatar(@Nullable String url, @NonNull ImageView imageView, @DrawableRes int defaultAvatar, @Nullable Transformation transformation) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestCreator creator = Picasso.with(IAppHelper.getContext()).load(getPath(url));
        if (defaultAvatar != 0) {
            creator = creator.placeholder(defaultAvatar).error(defaultAvatar);
        }
        if (transformation != null) {
            creator = creator.transform(transformation);
        }
        creator.into(imageView);
    }

    private static String getPath(String path) {
        return IAppUtils.isNetPath(path) ? path : Uri.fromFile(new File(path)).toString();
    }

}
