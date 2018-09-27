/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.activity.BDShadowActivity;
import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.compat.ImageCompat;
import com.baidaojuhe.library.baidaolibrary.dialog.BottomOpsDialog;
import com.baidaojuhe.library.baidaolibrary.impl.ActivityListener;
import com.baidaojuhe.library.baidaolibrary.impl.ContextExtend;
import com.baidaojuhe.library.baidaolibrary.util.ImageLoader;
import com.baidaojuhe.library.baidaolibrary.widget.itemdecoration.SpaceItemDecoration;

import net.box.app.library.IContext;
import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.util.IClickFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by box on 2017/3/29.
 * <p>
 * 显示照片
 */

@SuppressWarnings("unused")
public class PicturesView extends LinearLayout {

    private static final ScaleType[] SCALE_TYPES = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    public enum TakePictureMode {
        ALL, Album, Camera
    }

    private static final String DEFAULT_PLUS_ITEM = "plusItem";
    private static final Map<TakePictureMode, Integer> SOURCE_MAP = new HashMap<>(TakePictureMode.values().length);

    private static final int DEFAULT_SCALE_TYPE = 6;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private static final int DEFAULT_INTERVAL = 8;

    private static final int NO_LIMIT = -1;

    @Nullable
    private TextView mTvTitle;
    private RecyclerView mRvPictures;

    private int mTitleTextAppearance;
    private int mTitlteStyle;
    private float mItemWHRate;
    private int mScaleType;
    private boolean hasTitle;
    private int mLimit;
    @SuppressWarnings("FieldCanBeLocal")
    private int mTop, mBottom;

    private PicturesAdapter mPicturesAdapter;
    private GridLayoutManager mLayoutManager;

    private boolean isEditable;

    private List<String> mPaths = new CopyOnWriteArrayList<>();
    private TakePictureMode mTakePictureMode;
    @Nullable
    private Callback mCallback;
    @Nullable
    private RemoveCallback mRemoveCallback;
    @Nullable
    private View mOutsideEditView;

    private int mResultPicturePosition = -1;

    public PicturesView(@NonNull Context context) {
        this(context, null);
    }

    public PicturesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicturesView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(VERTICAL);
        setClipChildren(false);
        setClipToPadding(false);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PicturesView, defStyleAttr, 0);

        int horizontalSpacing = a.getDimensionPixelSize(R.styleable.PicturesView_horizontalSpacing, DEFAULT_INTERVAL);
        int verticalSpacing = a.getDimensionPixelSize(R.styleable.PicturesView_verticalSpacing, DEFAULT_INTERVAL);

        String title = a.getString(R.styleable.PicturesView_title);
        int spanCount = a.getInt(R.styleable.PicturesView_spanCount, DEFAULT_SPAN_COUNT);
        mTitleTextAppearance = a.getResourceId(R.styleable.PicturesView_titleTextAppearance, R.style.TextAppearance_AppCompat);
        mTitlteStyle = a.getResourceId(R.styleable.PicturesView_titleStyle, R.style.BD_AppTheme_TextView);
        mItemWHRate = a.getFloat(R.styleable.PicturesView_itemWHRate, 0.0f);
        int limit = a.getInt(R.styleable.PicturesView_limit, NO_LIMIT);
        boolean hasTitle = a.getBoolean(R.styleable.PicturesView_hasTitle, true);
        boolean isEditable = a.getBoolean(R.styleable.PicturesView_editable, false);

        mTop = getPaddingTop();
        mBottom = getPaddingBottom();

        removeAllViews();

        mRvPictures = new RecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mRvPictures, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mRvPictures.setAdapter(mPicturesAdapter = new PicturesAdapter());
        mRvPictures.addItemDecoration(new SpaceItemDecoration(horizontalSpacing, verticalSpacing, false));
        mRvPictures.setNestedScrollingEnabled(false);
        mRvPictures.setVisibility(GONE);
        mRvPictures.setFocusable(false);
        mRvPictures.setClipChildren(false);
        mRvPictures.setClipToPadding(false);

        setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);

        if (spanCount <= 0) {
            spanCount = 1;
        }
        mRvPictures.setLayoutManager(mLayoutManager = new GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false));

        mScaleType = a.getInt(R.styleable.PicturesView_itemScaleType, DEFAULT_SCALE_TYPE);

        setTakePictureMode(TakePictureMode.ALL);
        setSources(TakePictureMode.ALL, R.drawable.bd_btn_album);
        setSources(TakePictureMode.Album, R.drawable.bd_btn_album);
        setSources(TakePictureMode.Camera, R.drawable.bd_btn_camera);
        setTitle(isInEditMode() && TextUtils.isEmpty(title) ? "title" : title);
        setEditable(isEditable);
        setHasTitle(hasTitle);
        setLimit(isInEditMode() ? NO_LIMIT : limit);

        a.recycle();

        mRvPictures.setDuplicateParentStateEnabled(true);

        // setExitSharedElementCallback(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.setClipChildren(false);
            parent.setClipToPadding(false);
        }
    }

    public void setOnFileCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setOnFileRemoveCallback(RemoveCallback callback) {
        this.mRemoveCallback = callback;
    }

    @Override
    public void setOrientation(int orientation) {
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mTvTitle != null) {
            mTvTitle.setEnabled(enabled);
        }
        mRvPictures.setEnabled(enabled);
        mPicturesAdapter.notifyDataSetChanged();
    }

    public void setTitle(@StringRes int title) {
        setTitle(getContext().getText(title));
    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTvTitle == null) {
                mTvTitle = new AppCompatTextView(new ContextThemeWrapper(getContext(), mTitlteStyle));
                mTvTitle.setSingleLine();
                //noinspection deprecation
                mTvTitle.setTextAppearance(getContext(), mTitleTextAppearance);
                ViewGroup.LayoutParams layoutParams = mTvTitle.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = generateDefaultLayoutParams();
                }
                layoutParams.width = LayoutParams.MATCH_PARENT;
                addView(mTvTitle, 0, layoutParams);
            }
        } else if (mTvTitle != null) {
            removeView(mTvTitle);
            mTvTitle = null;
        }
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            mTvTitle.setBackgroundResource(0);
        }
        hasTitle = true;
        if (mTvTitle != null) {
            mTvTitle.setDuplicateParentStateEnabled(true);
        }
    }

    public void setLinkTextColor(int color) {
        if (mTvTitle != null) {
            mTvTitle.setLinkTextColor(color);
        }
    }

    public void setHighlightColor(int color) {
        if (mTvTitle != null) {
            mTvTitle.setHighlightColor(color);
        }
    }

    public void setMovementMethod(MovementMethod movement) {
        if (mTvTitle != null) {
            mTvTitle.setMovementMethod(movement);
        }
    }

    public void setSpanCount(int spanCount) {
        if (spanCount <= 0) {
            spanCount = 1;
        }
        mLayoutManager.setSpanCount(spanCount);
    }

    public void clear() {
        mPicturesAdapter.setNotifyOnChange(false);
        mPaths.clear();
        mPicturesAdapter.clear();
        if (isEditable) {
            mPicturesAdapter.set(DEFAULT_PLUS_ITEM);
        }
        mPicturesAdapter.notifyDataSetChanged();
    }

    public void remove(int position) {
        mPaths.remove(position);
        notifyDateSetChanged();
    }

    public void remove(String path) {
        mPaths.remove(path);
        notifyDateSetChanged();
    }

    public void setPicturePaths(@NonNull String... paths) {
        setPicturePaths(Arrays.asList(paths));
    }

    public void setPicturePaths(@NonNull Collection<String> paths) {
        mPicturesAdapter.setNotifyOnChange(false);
        mPicturesAdapter.set(paths);
        // mRvPictures.setVisibility(paths.isEmpty() && !isEditable ? GONE : VISIBLE);
        if (paths.isEmpty()) {
            mRvPictures.setVisibility(isEditable && mOutsideEditView == null ? VISIBLE : GONE);
        } else {
            mRvPictures.setVisibility(VISIBLE);
        }
        mPaths.clear();
        mPaths.addAll(paths);
        int size = paths.size();
        String item = mPicturesAdapter.getItem(size - 1);
        if (mOutsideEditView == null && isEditable && (mLimit <= NO_LIMIT || size < mLimit)) {
            if (!TextUtils.equals(item, DEFAULT_PLUS_ITEM)) {
                mPicturesAdapter.insert(DEFAULT_PLUS_ITEM, size);
            }
        } else {
            mPicturesAdapter.remove(DEFAULT_PLUS_ITEM);
        }
        mPicturesAdapter.notifyDataSetChanged();
    }

    public boolean replacePicturePath(String src, String target) {
        if (TextUtils.isEmpty(src)) {
            return false;
        }
        if (Collections.replaceAll(mPaths, src, target)) {
            PicturesView.this.notifyDateSetChanged();
        }
        return true;
    }

    public boolean setPicturePath(int index, String path) {
        if (TextUtils.isEmpty(path) || index < 0 || index >= mPaths.size()) {
            return false;
        }
        mPaths.set(index, path);
        PicturesView.this.notifyDateSetChanged();
        return true;
    }

    public List<String> getPicturePaths() {
        List<String> paths = new ArrayList<>(mPaths);
        paths.remove(DEFAULT_PLUS_ITEM);
        return paths;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        if (isInEditMode()) {
            int spanCount = mLayoutManager.getSpanCount();
            setPicturePaths(new String[isEditable && mOutsideEditView == null ? spanCount - 1 : spanCount]);
        }
        PicturesView.this.notifyDateSetChanged();
    }

    public void setTakePictureMode(@NonNull TakePictureMode mode) {
        this.mTakePictureMode = mode;
        if (isEditable) {
            mPicturesAdapter.notifyItemChanged(mPaths.size());
        }
    }

    public void setSources(@NonNull TakePictureMode mode, @DrawableRes int res) {
        SOURCE_MAP.put(mode, res);
        if (isEditable) {
            mPicturesAdapter.notifyItemChanged(mPaths.size());
        }
    }

    public void setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
        if (!hasTitle) {
            setTitle(null);
        }
        mRvPictures.setPadding(0, (hasTitle ? 0 : mTop), 0, mBottom);
    }

    public void setLimit(int limit) {
        this.mLimit = limit;
        PicturesView.this.notifyDateSetChanged();
    }

    public void setOutsideEditView(View outsideEditView) {
        this.mOutsideEditView = outsideEditView;
        if (isInEditMode()) {
            int spanCount = mLayoutManager.getSpanCount();
            setPicturePaths(new String[isEditable && mOutsideEditView == null ? spanCount - 1 : spanCount]);
        }
        notifyDateSetChanged();
        if (mOutsideEditView != null) {
            mOutsideEditView.setOnClickListener(v -> {
                if (isInEditMode()) {
                    return;
                }
                if (mTakePictureMode == TakePictureMode.Album) {
                    BDShadowActivity.Caller.call(BDShadowActivity.TYPE_ALBUM, mPicturesAdapter);
                } else if (mTakePictureMode == TakePictureMode.Camera) {
                    BDShadowActivity.Caller.call(BDShadowActivity.TYPE_CAMERA, mPicturesAdapter);
                } else if (mTakePictureMode == TakePictureMode.ALL) {
                    showAlbumCameraDialog(mPicturesAdapter);
                }
            });
        }
    }

    public void performEditClick() {
        performEditClick(mTakePictureMode);
    }

    public void performEditClick(TakePictureMode mode) {
        switch (mode) {
            case Album:
                performAlbumClick();
                break;
            case Camera:
                performCameraClick();
                break;
            case ALL:
                showAlbumCameraDialog(mPicturesAdapter);
                break;
        }
    }

    public void performAlbumClick() {
        BDShadowActivity.Caller.call(BDShadowActivity.TYPE_ALBUM, mPicturesAdapter);
    }

    public void performCameraClick() {
        BDShadowActivity.Caller.call(BDShadowActivity.TYPE_CAMERA, mPicturesAdapter);
    }

    private void notifyDateSetChanged() {
        setPicturePaths(new ArrayList<>(mPaths));
    }

    /**
     * Returns the image of interest to be used as the entering/returning
     * shared element during the activity transition.
     */
    @Nullable
    private View getImageAtPosition(int position) {
        String name = BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + mResultPicturePosition;
        return mRvPictures.findViewWithTag(name);
    }

    private void setExitSharedElementCallback(Context context) {
        if (!(context instanceof ContextExtend)) {
            return;
        }
        ContextExtend contextExtend = (ContextExtend) context;
        FragmentActivity activity = (FragmentActivity) (contextExtend).getActivity();
        activity.setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                View sharedElement = getImageAtPosition(mResultPicturePosition);
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                    return;
                }
                String transitionName = ViewCompat.getTransitionName(sharedElement);
                names.clear();
                names.add(transitionName);
                sharedElements.clear();
                sharedElements.put(transitionName, sharedElement);
            }
        });
        (contextExtend).addActivityListener(new ActivityListener.OnActivityReenterListener() {
            @Override
            public void onActivityReenter(int resultCode, Intent data) {
                if (data == null) {
                    return;
                }
                mResultPicturePosition = data.getIntExtra(BDConstants.BDKey.KEY_RESULT_PICTURE_POSITION, -1);
            }
        });
    }

    private void showAlbumCameraDialog(PicturesAdapter picturesAdapter) {
        BottomOpsDialog dialog = new BottomOpsDialog(getContext());
        dialog.addAll(R.array.bd_array_camera_album);
        dialog.setOnItemClickListener((dialog1, view1, position1) -> {
            switch (position1) {
                case 0:
                    performAlbumClick();
                    break;
                case 1:
                    performCameraClick();
                    break;
            }
        });
        dialog.show();
    }

    private class ItemView extends AppCompatImageButton {

        @Nullable
        private String mPath;
        private int mWidth, mHeight;
        @DrawableRes
        private int mResId;

        public ItemView(Context context) {
            super(context);
        }

        public void setImagePath(@NonNull String imagePath) {
            this.mPath = imagePath;
            this.mResId = 0;
            notifyDateSetChanged();
        }

        @Override
        public void setImageResource(@DrawableRes int resId) {
            this.mPath = null;
            this.mResId = resId;
            notifyDateSetChanged();
        }

        @Override
        public void setImageDrawable(@Nullable Drawable drawable) {
            super.setImageDrawable(drawable);
        }

        @Override
        public void setImageURI(@Nullable Uri uri) {
            mPath = null;
            super.setImageURI(uri);
        }

        @Override
        public void setImageBitmap(Bitmap bm) {
            mPath = null;
            super.setImageBitmap(bm);
        }

        @Override
        public void setImageIcon(@Nullable Icon icon) {
            mPath = null;
            super.setImageIcon(icon);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (mItemWHRate <= 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = Math.round(width * mItemWHRate);
                setMeasuredDimension(width, height);
            }
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            notifyDateSetChanged();
        }

        private void notifyDateSetChanged() {
            if (!TextUtils.isEmpty(mPath)) {
                if (mWidth != 0 && mHeight != 0) {
                    ImageLoader.into(getContext(), mPath, this, mWidth, mHeight);
                }
            } else if (mResId != 0) {
                super.setImageResource(mResId);
            }
        }
    }

    private class PicturesAdapter extends ArrayAdapter<String, ViewHolder> implements BDShadowActivity.Callback<File> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FrameLayout layout = new FrameLayout(getContext());
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            return new ViewHolder(getContext(), layout);
        }

        @Override
        public void onCallback(@Nullable File result) {
            boolean intercept = false;
            if (mCallback != null) {
                intercept = mCallback.onCallback(result);
            }
            if (intercept || result == null || !result.exists()) {
                return;
            }
            String absolutePath = result.getAbsolutePath();
            if (!mPaths.contains(absolutePath)) {
                mPaths.add(absolutePath);
                PicturesView.this.notifyDateSetChanged();
            }
        }
    }

    private class ViewHolder extends BaseViewHolder {

        private static final int DELETE_PADDING = 4;
        private static final int DELETE_MARGIN = 4;

        private ItemView ibPicture;
        private ImageButton ibDelete;

        ViewHolder(Context context, FrameLayout itemView) {
            super(context, itemView);

            this.ibPicture = new ItemView(getContext());
            ibPicture.setScaleType(SCALE_TYPES[mScaleType]);
            if (isInEditMode()) {
                ibPicture.setBackgroundColor(Color.CYAN);
            } else {
                ibPicture.setBackgroundColor(Color.TRANSPARENT);
            }
            IClickFilter.filterForeground(ibPicture);

            this.ibDelete = new ImageButton(getContext());
            ibDelete.setBackgroundColor(Color.TRANSPARENT);
            ibDelete.setImageResource(R.drawable.bd_btn_delete);
            ibDelete.setScaleType(ScaleType.CENTER_CROP);
            ibDelete.setPadding(DELETE_PADDING, DELETE_PADDING, DELETE_PADDING, DELETE_PADDING);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.addView(ibPicture, layoutParams);

            FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            deleteParams.gravity = GravityCompat.END;
            int margin = (int) -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DELETE_MARGIN, getResources().getDisplayMetrics());
            deleteParams.topMargin = margin;
            deleteParams.rightMargin = margin;
            itemView.addView(ibDelete, deleteParams);
        }

        @Override
        public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
            ibPicture.setEnabled(isEnabled());
            ibDelete.setEnabled(isEnabled());

            PicturesAdapter picturesAdapter = (PicturesAdapter) adapter;
            String path = picturesAdapter.getItem(position);
            if (TextUtils.equals(DEFAULT_PLUS_ITEM, path)) {
                ibPicture.setScaleType(ScaleType.CENTER_CROP);
                ibPicture.setImageResource(SOURCE_MAP.get(mTakePictureMode));
                ibPicture.setBackgroundColor(Color.TRANSPARENT);
                ibDelete.setVisibility(INVISIBLE);
                ibDelete.setOnClickListener(null);
                ibPicture.setOnClickListener((view) -> {
                    if (isInEditMode()) {
                        return;
                    }
                    if (mTakePictureMode == TakePictureMode.Album) {
                        BDShadowActivity.Caller.call(BDShadowActivity.TYPE_ALBUM, picturesAdapter);
                    } else if (mTakePictureMode == TakePictureMode.Camera) {
                        BDShadowActivity.Caller.call(BDShadowActivity.TYPE_CAMERA, picturesAdapter);
                    } else if (mTakePictureMode == TakePictureMode.ALL) {
                        showAlbumCameraDialog(picturesAdapter);
                    }
                });
            } else {
                if (!isInEditMode()) {
                    ibPicture.setImagePath(path);
                }
                ibPicture.setScaleType(SCALE_TYPES[mScaleType]);
                ibDelete.setVisibility(isEditable ? VISIBLE : INVISIBLE);
                ibPicture.setOnClickListener(v -> {
                    mResultPicturePosition = position;
                    ImageCompat.openImages((IContext) getContext(), v, position, mPaths);
                });
                ibDelete.setOnClickListener((view) -> {
                    boolean intercept = false;
                    if (mRemoveCallback != null) {
                        intercept = mRemoveCallback.onCallback(path, position);
                    }
                    if (intercept) {
                        return;
                    }
                    mPaths.remove(getAdapterPosition());
                    PicturesView.this.notifyDateSetChanged();
                });
            }

            String transitionName = BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + position;
            ViewCompat.setTransitionName(ibPicture, transitionName);
            ibPicture.setTag(transitionName);
        }
    }

    public interface Callback {
        boolean onCallback(@Nullable File file);
    }

    public interface RemoveCallback {
        boolean onCallback(@NonNull String file, int index);
    }

}
