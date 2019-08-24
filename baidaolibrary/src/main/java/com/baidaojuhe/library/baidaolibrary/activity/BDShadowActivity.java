/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDRequestCode;
import com.baidaojuhe.library.baidaolibrary.compat.ToastCompat;
import com.baidaojuhe.library.baidaolibrary.util.BitmapUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IFileUtils;
import net.box.app.library.util.ILogCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by box on 2017/4/11.
 * <p>
 * 空白页面，处理回调
 */

@SuppressWarnings("unused")
public class BDShadowActivity extends AppCompatActivity {

    private static final String TAG = BDShadowActivity.class.getSimpleName();

    private static final int TYPE_DEFAULT = -1;
    public static final int TYPE_CAMERA = 0x0;
    public static final int TYPE_ALBUM = 0x1;

    private File mSdcardTempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int requestType = getIntent().getIntExtra(BDKey.KEY_REQUEST_TYPE, TYPE_DEFAULT);
        if (requestType == TYPE_DEFAULT) {
            EventBus.getDefault().post(new Result<>(null));
            finish();
            return;
        }
        switch (requestType) {
            case TYPE_CAMERA:
                mSdcardTempFile = new File(IAppUtils.getCachePath(this), System.currentTimeMillis() + ".jpg");
                RxPermissions.getInstance(this).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (!aBoolean) {
                        ToastCompat.showText(R.string.bd_prompt_no_permission);
                        finish();
                        return;
                    }

                    boolean isFontCamera = getIntent().getBooleanExtra(BDKey.KEY_FONT_CAMERA, false);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        finish();
                        return;
                    }
                    IAppUtils.openCamera(this, BDRequestCode.REQUEST_CODE_CAMERA, isFontCamera, mSdcardTempFile);
                    // CameraProvider.openCamera(this, BDRequestCode.REQUEST_CODE_CAMERA, isFontCamera, mSdcardTempFile, Configuration.ORIENTATION_PORTRAIT);
                });
                break;
            case TYPE_ALBUM:
                RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (!aBoolean) {
                        ToastCompat.showText(R.string.bd_prompt_no_permission);
                        finish();
                        return;
                    }

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        finish();
                        return;
                    }
                    IAppUtils.openPicture(this, BDRequestCode.REQUEST_CODE_ALBUM);
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if (resultCode != RESULT_OK) {
        //     mSdcardTempFile = null;
        //     finish();
        //     return;
        // }
        boolean isResult = resultCode == RESULT_OK;
        switch (requestCode) {
            case BDRequestCode.REQUEST_CODE_CAMERA:
                mSdcardTempFile = BitmapUtils.compressImage(mSdcardTempFile, mSdcardTempFile.getPath(), 20);
                ILogCompat.e(TAG, isResult ? mSdcardTempFile : "拍照已取消");
                EventBus.getDefault().post(new Result<>(isResult ? mSdcardTempFile : (mSdcardTempFile = null)));
                break;
            case BDRequestCode.REQUEST_CODE_ALBUM:
                Uri imgeUri = data == null ? null : data.getData();
                String path = imgeUri == null ? null : IFileUtils.GetPathFromUri4kitkat.getPath(this, imgeUri);
                ILogCompat.e(TAG, isResult ? mSdcardTempFile : "获取图片已失败");
                if (!TextUtils.isEmpty(path)) {
                    mSdcardTempFile = new File(IAppUtils.getCachePath(this), System.currentTimeMillis() + ".jpg");
                    mSdcardTempFile = BitmapUtils.compressImage(new File(path), mSdcardTempFile.getPath(), 20);
                }
                EventBus.getDefault().post(new Result<>(isResult ? mSdcardTempFile : (mSdcardTempFile = null)));
                break;
        }
        finish();
    }

    @SuppressWarnings("WeakerAccess")
    public static class Caller<ResultType> {

        private Callback<ResultType> mCallback;

        private Caller() {
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEventResult(Result<ResultType> result) {
            EventBus.getDefault().unregister(this);
            if (mCallback != null) {
                mCallback.onCallback(result.object);
            }
            mCallback = null;
        }

        private Caller register(Callback<ResultType> callback) {
            this.mCallback = callback;
            EventBus.getDefault().register(this);
            return this;
        }

        public static <T> void call(@RequestType int requestType, Callback<T> callback) {
            call(requestType, false, callback);
        }

        public static <T> void call(@RequestType int requestType, boolean isFontCamera, Callback<T> callback) {
            new Caller<T>().register(callback);
            Bundle bundle = new Bundle();
            bundle.putInt(BDKey.KEY_REQUEST_TYPE, requestType);
            bundle.putBoolean(BDKey.KEY_FONT_CAMERA, isFontCamera);

            Intent intent = new Intent(IAppHelper.getContext(), BDShadowActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            IAppHelper.getContext().startActivity(intent);
        }
    }

    public interface Callback<T> {
        void onCallback(@Nullable T result);
    }

    private class Result<T> {

        public final T object;

        Result(T object) {
            this.object = object;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_CAMERA, TYPE_ALBUM})
    private @interface RequestType {
    }
}
