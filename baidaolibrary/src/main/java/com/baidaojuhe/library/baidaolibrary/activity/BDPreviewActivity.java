/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDRequestCode;
import com.baidaojuhe.library.baidaolibrary.compat.DownloadCompat;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.compat.VideoCompat;
import com.baidaojuhe.library.baidaolibrary.dialog.BottomOpsDialog;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.util.DownloadUtils;
import com.google.gson.reflect.TypeToken;

import net.box.app.library.util.IJsonDecoder;
import net.box.app.library.util.IMediaFile;
import net.box.app.library.util.IPermissionCompat;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import rx.Observer;

/**
 * Created by box on 2017/5/22.
 * <p>
 * 图片预览
 */

@SuppressWarnings({"unused", "SwitchStatementWithTooFewBranches"})
public class BDPreviewActivity extends BDActionBarActivity implements View.OnClickListener,
        View.OnLongClickListener, BottomOpsDialog.OnItemClickListener, Observer<File>,
        ViewPager.OnPageChangeListener {

    public static final int PAGE_MARGIN = 40;

    private GalleryViewPager mVpImage;

    private ArrayList<String> mImagePaths = new ArrayList<>();

    private BottomOpsDialog mOperationDialog;
    private int mStartPosition;
    private int mCurrentPosition = -1;

    @Override
    public void onInitViews(@NonNull Bundle arguments, Bundle savedInstanceState) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        decorView.setFitsSystemWindows(true);

        mOperationDialog = new BottomOpsDialog(this);

        ActivityCompat.postponeEnterTransition(this);
        // setEnterSharedElementCallback(mCallback);

        mVpImage = IViewCompat.findById(this, R.id.bd_vp_image);
    }

    @Override
    public void onInitDatas(@NonNull Bundle arguments, Bundle savedInstanceState) {
        String[] imagePaths = null;

        Uri uri = getIntent().getData();
        String imagePreviewScheme = "imagepreview";
        String scheme;
        if (uri != null && (scheme = uri.getScheme()) != null && scheme.equals(imagePreviewScheme)) {
            String paths = uri.getQueryParameter(BDKey.KEY_IMAGE_PATHS);
            try {
                paths = URLDecoder.decode(paths, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<String> pathList = IJsonDecoder.jsonToObject(paths, new TypeToken<List<String>>() {
            }.getType());
            if (pathList != null) {
                imagePaths = pathList.toArray(new String[0]);
            }
            final String position = uri.getQueryParameter(BDKey.KEY_CURRENT_POSITION);
            mStartPosition = position != null ? Integer.valueOf(position) : 0;
        } else {
            imagePaths = getBundle().getStringArray(BDKey.KEY_IMAGE_PATHS);
            mStartPosition = getBundle().getInt(BDKey.KEY_CURRENT_POSITION);
        }

        mImagePaths.clear();
        if (imagePaths != null) {
            mImagePaths.addAll(Arrays.asList(imagePaths));
        }

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, mImagePaths);
        mVpImage.setPageMargin(PAGE_MARGIN);
        mVpImage.setOffscreenPageLimit(3);
        mVpImage.setAdapter(pagerAdapter);
        pagerAdapter.setOnClickListener(this);
        pagerAdapter.setOnLongClickListener(this);

        mOperationDialog.addAll(R.array.bd_array_download_menus);

        ViewCompat.setTransitionName(mVpImage, BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + mStartPosition);

        ActivityCompat.startPostponedEnterTransition(this);
    }

    @Override
    public void onInitListeners(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mOperationDialog.setOnItemClickListener(this);
        mVpImage.addOnPageChangeListener(this);
        mVpImage.setCurrentItem(mStartPosition, false);
    }

    @Override
    public BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_preview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (IPermissionCompat.hasSelfPermission(grantResults)) {
            int currentItem = mVpImage.getCurrentItem();
            //noinspection MissingPermission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            DownloadCompat.download(mImagePaths.get(currentItem), this, mProgressListener);
        } else {
            showText(R.string.bd_prompt_no_permission);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                int currentItem = mVpImage.getCurrentItem();
                String url = mImagePaths.get(currentItem);
                if (IMediaFile.isVideoFileType(url)) {
                    VideoCompat.openVideo(this, url, null);
                } else {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra(BDKey.KEY_RESULT_PICTURE_POSITION, mCurrentPosition);
        setResult(RESULT_OK, intent);
        super.finishAfterTransition();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            default:
                mOperationDialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(DialogInterface dialog, View view, int position) {
        if (position == 0) {
            int currentItem = mVpImage.getCurrentItem();
            String url = mImagePaths.get(currentItem);
            if (IPermissionCompat.checkSelfPermission((Activity) this,
                    BDRequestCode.RESUEST_CODE_DOWNLOAD_FILE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                DownloadCompat.download(url, this, mProgressListener);
            }
        }
    }

    @Override
    public void onCompleted() {
        loadDismiss();
    }

    @Override
    public void onError(Throwable e) {
        loadDismiss();
        showText(e.getMessage());
    }

    @Override
    public void onNext(File file) {
        showText(R.string.bd_prompt_file_download_success_to_dir, file.getAbsolutePath());
    }

    private DownloadUtils.DownloadProgressListener mProgressListener = (bytesRead, contentLength, done) -> {
        int count = (int) ((double) bytesRead * 1.0D / (double) contentLength * 100.0D);
        showLoad(String.format(getString(R.string.bd_prompt_downing_), count + "%"));
    };

    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            View sharedElement = mVpImage.getChildAt(mCurrentPosition);
            if (sharedElement != null && mStartPosition != mCurrentPosition) {
                String transitionName = ViewCompat.getTransitionName(sharedElement);
                names.clear();
                names.add(transitionName);
                sharedElements.clear();
                if (transitionName != null) {
                    sharedElements.put(transitionName, sharedElement);
                }
            }
        }
    };

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
