/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.GalleryWidget;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import net.box.app.library.IContext;

import java.util.List;

/**
 * Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b>
 * objects to paging up through them.
 */
@SuppressWarnings("unused")
class BasePagerAdapter extends PagerAdapter {

    final List<String> mResources;
    protected final IContext mContext;
    private int mCurrentPosition = -1;
    private OnItemChangeListener mOnItemChangeListener;

    public BasePagerAdapter() {
        mResources = null;
        mContext = null;
    }

    BasePagerAdapter(IContext context, List<String> resources) {
        this.mResources = resources;
        this.mContext = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, final int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (mCurrentPosition == position) {
            return;
        }
        GalleryViewPager galleryContainer = ((GalleryViewPager) container);
        if (galleryContainer.mCurrentView != null) {
            galleryContainer.mCurrentView.resetScale();
        }
        mCurrentPosition = position;
        if (mOnItemChangeListener != null) {
            mOnItemChangeListener.onItemChange(mCurrentPosition);
        }
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mResources != null) {
            return mResources.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        mOnItemChangeListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnItemChangeListener {
        void onItemChange(int currentPosition);
    }
}