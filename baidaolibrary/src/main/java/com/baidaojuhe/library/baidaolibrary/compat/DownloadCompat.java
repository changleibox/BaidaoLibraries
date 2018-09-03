/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.baidaojuhe.library.baidaolibrary.util.DownloadUtils;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IMediaFile;

import java.io.File;

import rx.Observer;
import rx.Subscriber;

/**
 * Created by Box on 16/9/19.
 * <p/>
 * 文件下载
 */
@SuppressWarnings({"deprecation", "WeakerAccess"})
public class DownloadCompat {

    public static final String DIR_PATH = IAppUtils.getSDPath() + "/" + IAppHelper.getContext().getPackageName() + "/";

    public static String getFilePath(@NonNull String url) {
        if (!IAppUtils.isNetPath(url) && new File(url).exists()) {
            return url;
        }
        String dir;
        if (IMediaFile.isVideoFileType(url)) {
            dir = "Video";
        } else if (IMediaFile.isImageFileType(url)) {
            dir = "Image";
        } else if (IMediaFile.isAudioFileType(url)) {
            dir = "Audio";
        } else {
            dir = "Temp";
        }
        return DIR_PATH + dir + url.substring(url.lastIndexOf("/"));
    }

    public static File getLocalFile(@NonNull String url) {
        File file = new File(getFilePath(url));
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static void download(@NonNull String url, final Observer<File> callback, final DownloadUtils.DownloadProgressListener specialCallback) {
        File temporaryFile = new File(getFilePath(url));
        if (temporaryFile.exists()) {
            if (callback != null) {
                callback.onNext(temporaryFile);
                callback.onCompleted();
            }
            return;
        }
        DownloadUtils utils = new DownloadUtils(null, specialCallback);
        utils.download(url, temporaryFile, new Subscriber<File>() {
            @Override
            public void onCompleted() {
                if (callback != null) {
                    callback.onCompleted();
                }
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                temporaryFile.delete();
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onNext(File file) {
                if (callback != null) {
                    callback.onNext(temporaryFile);
                }
            }
        });
    }

}
