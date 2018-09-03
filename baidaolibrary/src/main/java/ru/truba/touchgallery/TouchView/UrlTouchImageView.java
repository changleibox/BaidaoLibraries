/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.TouchView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.VideoCompat;
import com.baidaojuhe.library.baidaolibrary.util.ImageLoader;

import net.box.app.library.IContext;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.ICircleTransform;
import net.box.app.library.util.IMediaFile;

import java.io.File;
import java.net.URLDecoder;

@SuppressLint("ViewConstructor")
public class UrlTouchImageView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {

    protected ProgressBar mProgressBar;
    protected TouchImageView mImageView;
    protected ImageView mPlay;

    protected IContext mContext;
    private OnClickListener mClick;
    private OnLongClickListener mLongClick;
    private boolean isCircle;

    public UrlTouchImageView(IContext ctx) {
        this(ctx, false);
    }

    public UrlTouchImageView(IContext ctx, boolean isCircle) {
        super(ctx.getActivity());
        mContext = ctx;
        this.isCircle = isCircle;
        init();
    }

    public UrlTouchImageView(IContext ctx, AttributeSet attrs) {
        super(ctx.getActivity(), attrs);
        mContext = ctx;
        init();
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    protected void init() {
        mImageView = new TouchImageView(mContext.getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);

        mProgressBar = new ProgressBar(mContext.getActivity(), null, android.R.attr.progressBarStyleSmall);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(100);
        this.addView(mProgressBar);

        mPlay = new ImageButton(mContext.getActivity());
        mPlay.setBackgroundColor(Color.TRANSPARENT);
        mPlay.setImageResource(R.drawable.bd_btn_play);
        mPlay.setOnClickListener(this);
        mPlay.setScaleType(ScaleType.FIT_CENTER);
        mPlay.setVisibility(GONE);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(mPlay, params);
    }

    public void setUrl(String imageUrl) {
        mImageView.setOnClickListener(this);
        mImageView.setOnLongClickListener(this);
        if (TextUtils.isEmpty(imageUrl)) {
            onImageError();
            return;
        }

        imageUrl = IAppUtils.isNetPath(imageUrl) ? imageUrl : Uri.fromFile(new File(imageUrl)).toString();

        try {
            imageUrl = URLDecoder.decode(imageUrl, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUrl = imageUrl.split(" ")[0];
        if (IMediaFile.isImageFileType(imageUrl)) {
            mImageView.setZoomToOriginalSize(true);
            new ImageLoadTask().execute(imageUrl);
        } else if (IMediaFile.isVideoFileType(imageUrl)) {
            mPlay.setVisibility(VISIBLE);
            mImageView.setZoomToOriginalSize(false);
            VideoCompat.getVideoThumbnail(imageUrl, mImageView, mPlay, mProgressBar, ScaleType.MATRIX, false);
        } else {
            onImageError();
        }
    }

    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    @Override
    public void onClick(View v) {
        if (mClick != null) {
            mClick.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mLongClick != null && mLongClick.onLongClick(v);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mClick = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        this.mLongClick = l;
    }

    private void onImageError() {
        mPlay.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        mImageView.setVisibility(GONE);
    }

    @SuppressLint("StaticFieldLeak")
    public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return ImageLoader.get(mContext, strings[0], 0, isCircle ? new ICircleTransform(ICircleTransform.CIRCLE) : null);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setScaleType(ScaleType.MATRIX);
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
            mImageView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }
    }

}
