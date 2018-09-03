/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.GalleryWidget;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.IContext;

import java.util.List;

import ru.truba.touchgallery.TouchView.UrlTouchImageView;

/**
 * Class wraps URLs to adapter, then it instantiates
 * {@link ru.truba.touchgallery.TouchView.UrlTouchImageView} objects to paging
 * up through them.
 */
public class UrlPagerAdapter extends BasePagerAdapter {

    private OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    public UrlPagerAdapter(IContext context, List<String> resources) {
        super(context, resources);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object == null) {
            return;
        }
        try {
            ((GalleryViewPager) container).mCurrentView = ((UrlTouchImageView) object).getImageView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        UrlTouchImageView imageView = new UrlTouchImageView(mContext);
        collection.addView(imageView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if (mResources != null) {
            imageView.setUrl(mResources.get(position));
        }

        imageView.setOnClickListener(mClickListener);
        imageView.setOnLongClickListener(mLongClickListener);
        String transitionName = BDConstants.BDTransitionName.TRANSITION_NAME_PREVIEW + position;
        ViewCompat.setTransitionName(imageView, transitionName);
        return imageView;
    }

    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    public void setOnLongClickListener(View.OnLongClickListener l) {
        mLongClickListener = l;
    }
}
