/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;
import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IAppUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Box on 16/9/20.
 * <p/>
 * 处理视频相关
 */
@SuppressWarnings("WeakerAccess")
public class VideoCompat {

    public static final String DIR_PATH = IAppHelper.getContext().getCacheDir().getAbsolutePath();

    public static String getFilePath(@NonNull String url, long timeUs) {
        return DIR_PATH + url.substring(url.lastIndexOf("/"), url.lastIndexOf(".")) + "_" + timeUs;
    }

    public interface Callback {
        void onCallback(Bitmap bitmap);
    }

    private static final Map<String, Bitmap> BITMAP_MAP = new WeakHashMap<>();

    public static Bitmap getThumbnail(String url) {
        return BITMAP_MAP.get(url);
    }

    public static void openVideo(IContext context, String url, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(BDConstants.BDKey.KEY_VIDEO_URL, url);
        context.startActivity(new Intent(BDConstants.BDIntentAction.ACTION_VIDEO).putExtras(bundle));
    }

    public static void getVideoThumbnail(@NonNull String url, @NonNull ImageView imageView, Callback callback, long timeUs) {
        getVideoThumbnail(url, imageView, null, null, null, false, timeUs, callback);
    }

    public static void getVideoThumbnail(@NonNull String url, @NonNull ImageView imageView, View play, View progressBar, ScaleType scaleType, boolean isSetSize) {
        getVideoThumbnail(url, imageView, play, progressBar, scaleType, isSetSize, -1, null);
    }

    public static void getVideoThumbnail(@NonNull String url, @NonNull ImageView imageView, View play, View progressBar, ScaleType scaleType, boolean isSetSize, long timeUs, Callback callback) {
        if (DownloadCompat.getLocalFile(url) != null) {
            url = DownloadCompat.getFilePath(url);
        }
        new ThumbnailTask().execute(url, imageView, play, progressBar, scaleType, isSetSize, callback, timeUs);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap createVideoThumbnail(String url, long timeUs) {
        Bitmap bitmap = null;
        String filePath = getFilePath(url, timeUs);
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (bitmap != null) {
            return bitmap;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        FileOutputStream fileOutputStream = null;
        try {
            if (IAppUtils.isNetPath(url)) {
                retriever.setDataSource(url, new HashMap<>());
            } else {
                retriever.setDataSource(url);
            }
            if (timeUs < 0) {
                bitmap = retriever.getFrameAtTime();
            } else {
                bitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST);
            }
            if (bitmap != null) {
                fileOutputStream = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private static class ThumbnailTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView mImageView;
        private View mPlay;
        private View mProgressBar;
        private ScaleType mScaleType;
        private boolean isSetSize;
        private Callback mCallback;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String url = params[0].toString();
            mImageView = (ImageView) params[1];
            mPlay = (View) params[2];
            mProgressBar = (View) params[3];
            mScaleType = (ScaleType) params[4];
            isSetSize = (boolean) params[5];
            mCallback = (Callback) params[6];
            long timeUs = (Long) params[7];
            String key = url + timeUs;
            Bitmap thumbnail = BITMAP_MAP.get(key);
            if (thumbnail == null) {
                thumbnail = createVideoThumbnail(url, timeUs);
            }
            if (thumbnail != null) {
                BITMAP_MAP.put(key, thumbnail);
            }
            return thumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mPlay != null) {
                mPlay.setVisibility(View.VISIBLE);
            }
            if (mScaleType != null) {
                mImageView.setScaleType(mScaleType);
            }
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
            mImageView.setVisibility(View.VISIBLE);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
            if (isSetSize) {
                int width = mImageView.getWidth();
                ViewGroup.LayoutParams params = mImageView.getLayoutParams();
                if (bitmap == null) {
                    params.height = width * 9 / 16;
                } else {
                    params.height = width * bitmap.getHeight() / bitmap.getWidth();
                }
                mImageView.setLayoutParams(params);
            }
            if (mCallback != null) {
                mCallback.onCallback(bitmap);
            }
        }
    }
}
