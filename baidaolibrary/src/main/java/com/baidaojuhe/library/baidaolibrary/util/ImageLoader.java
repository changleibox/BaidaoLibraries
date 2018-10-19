/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.widget.ImageView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import net.box.app.library.IContext;
import net.box.app.library.util.IAppUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by box on 2017/4/5.
 * <p>
 * 加载图片
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ImageLoader {

    private static final Map<String, Bitmap> CACHES_BITMAP_MAP = new WeakHashMap<>();

    @WorkerThread
    public static Bitmap get(IContext iContext, String avatar, int defAvatar) {
        Bitmap bitmap = CACHES_BITMAP_MAP.get(avatar);
        if (bitmap == null) {
            bitmap = get(iContext, avatar, defAvatar, null);
            if (bitmap != null) {
                CACHES_BITMAP_MAP.put(avatar, bitmap);
            }
        }
        return bitmap;
    }

    @WorkerThread
    public static Bitmap get(IContext iContext, String avatar, int defAvatar, @Nullable Transformation transformation) {
        Bitmap bitmap = null;
        RequestCreator requestCreator = createRequestCreator(iContext, avatar, defAvatar, 0, 0, transformation);
        try {
            bitmap = requestCreator == null ? null : requestCreator.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void into(@NonNull Context context, @NonNull String path, @NonNull ImageView view) {
        into(context, path, view, null);
    }

    public static void into(@NonNull Context context, @NonNull String path, @NonNull ImageView view, @Nullable Transformation transformation) {
        IViewCompat.addOnceOnGlobalLayoutListener(view, () -> {
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            into(context, path, view, width, height, transformation);
        });
    }

    public static void into(@NonNull Context context, @NonNull String path, @NonNull ImageView view,
                            @IntRange(from = 1) int width, @IntRange(from = 1) int height) {
        into(context, path, view, width, height, null);
    }

    public static void into(@NonNull Context context, @NonNull String path, @NonNull ImageView view,
                            @IntRange(from = 1) int width, @IntRange(from = 1) int height, @Nullable Transformation transformation) {
        into(context, path, R.drawable.box_default_avatar_rect, view, width, height, transformation);
    }

    public static void into(@NonNull Context context, @NonNull String path, @DrawableRes int defImage, @NonNull ImageView view) {
        into(context, path, defImage, view, null);
    }

    public static void into(@NonNull Context context, @NonNull String path, @DrawableRes int defImage, @NonNull ImageView view, @Nullable Transformation transformation) {
        IViewCompat.addOnceOnGlobalLayoutListener(view, () -> {
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            into(context, path, defImage, view, width, height, transformation);
        });
    }

    public static void into(@NonNull Context context, @NonNull String path, @DrawableRes int defImage, @NonNull ImageView view,
                            @IntRange(from = 1) int width, @IntRange(from = 1) int height) {
        into(context, path, defImage, view, width, height, null);
    }

    public static void into(@NonNull Context context, @Nullable String path, @DrawableRes int defImage, @NonNull ImageView view,
                            @IntRange(from = 1) int width, @IntRange(from = 1) int height, @Nullable Transformation transformation) {
        if (TextUtils.isEmpty(path)) {
            view.setImageResource(defImage);
            return;
        }
        RequestCreator creator = getCreator(context, path, defImage, getTransformation(width, height, transformation));
        // if (width > 0 && height > 0) {
        //     creator = creator.resize(width, height).centerCrop();
        // }
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        creator.into(view);
    }

    private static RequestCreator getCreator(Context context, @NonNull String path, @DrawableRes int defImage, @Nullable Transformation transformation) {
        Picasso picasso = new Picasso.Builder(context).build();
        // Picasso.setSingletonInstance(picasso);
        RequestCreator creator;
        if (IAppUtils.isNetPath(path)) {
            creator = picasso.load(path);
        } else {
            creator = picasso.load(new File(path));
        }
        if (defImage != 0) {
            creator = creator.error(defImage).placeholder(defImage);
        }
        return transformation == null ? creator : creator.transform(transformation);
    }

    private static Transformation getTransformation(int targetWidth, int targetHeight, Transformation transformation) {
        return new Transformation() {
            @SuppressWarnings("UnnecessaryLocalVariable")
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap result = centerCrop(source, targetWidth, targetHeight);
                return transformation == null ? result : transformation.transform(result);
            }

            @Override
            public String key() {
                return transformation == null ? "transformation" : transformation.key();
            }
        };
    }

    @SuppressWarnings("ConstantConditions")
    private static Bitmap centerCrop(Bitmap source, int targetWidth, int targetHeight) {
        int inWidth = source.getWidth();
        int inHeight = source.getHeight();

        int drawX = 0;
        int drawY = 0;
        int drawWidth = inWidth;
        int drawHeight = inHeight;

        Matrix matrix = new Matrix();

        float widthRatio = targetWidth / (float) inWidth;
        float heightRatio = targetHeight / (float) inHeight;
        float scaleX, scaleY;
        if (widthRatio > heightRatio) {
            int newSize = (int) Math.ceil(inHeight * (heightRatio / widthRatio));
            drawY = (inHeight - newSize) / 2;
            drawHeight = newSize;
            scaleX = widthRatio;
            scaleY = targetHeight / (float) drawHeight;
        } else {
            int newSize = (int) Math.ceil(inWidth * (widthRatio / heightRatio));
            drawX = (inWidth - newSize) / 2;
            drawWidth = newSize;
            scaleX = targetWidth / (float) drawWidth;
            scaleY = heightRatio;
        }
        if (shouldResize(false, inWidth, inHeight, targetWidth, targetHeight)) {
            matrix.preScale(scaleX, scaleY);
        }

        Bitmap newResult = Bitmap.createBitmap(source, drawX, drawY, drawWidth, drawHeight, matrix, true);
        if (newResult != source) {
            source.recycle();
            source = newResult;
        }

        return source;
    }

    private static boolean shouldResize(boolean onlyScaleDown, int inWidth, int inHeight,
                                        int targetWidth, int targetHeight) {
        return !onlyScaleDown || inWidth > targetWidth || inHeight > targetHeight;
    }

    @Nullable
    public static RequestCreator createRequestCreator(IContext iContext, String avatar, int defAvatar, int width, int height, Transformation transformation) {
        if (TextUtils.isEmpty(avatar)) {
            return null;
        }
        RequestCreator creator = new Picasso.Builder(iContext.getActivity()).build().load(avatar);
        if (defAvatar != 0) {
            creator = creator.placeholder(defAvatar).error(defAvatar);
        }
        if (width != 0 && height != 0) {
            creator = creator.resize(width, height).centerCrop();
        }
        return transformation == null ? creator : creator.transform(transformation);
    }

}
