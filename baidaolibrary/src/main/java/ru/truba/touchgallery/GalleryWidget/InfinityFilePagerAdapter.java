/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.GalleryWidget;

import android.view.ViewGroup;
import android.widget.ImageView;

import net.box.app.library.IContext;

import java.util.List;

import ru.truba.touchgallery.TouchView.FileTouchImageView;

/**
 * Class wraps file paths to adapter, then it instantiates
 * {@link ru.truba.touchgallery.TouchView.FileTouchImageView} objects to paging
 * up through them.
 */
public class InfinityFilePagerAdapter extends BasePagerAdapter {

    private int TOTAL_PAGES = -1;
    private int MIN_LOOPS = 1000;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public int FIRST_PAGE = 1;

    private ImageView.ScaleType mScaleType = null;

    public InfinityFilePagerAdapter(IContext context, List<String> resources) {
        super(context, resources);
        TOTAL_PAGES = resources.size();
        FIRST_PAGE = TOTAL_PAGES * MIN_LOOPS / 2;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager) container).mCurrentView = ((FileTouchImageView) object)
                .getImageView();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final FileTouchImageView iv = new FileTouchImageView(mContext);

        position = position % TOTAL_PAGES;

        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        if (mScaleType != null)
            iv.setScaleType(mScaleType);
        collection.addView(iv, 0);
        return iv;
    }

    /**
     * Set Scaletype for ImageView
     *
     * @param scaletype
     */
    public void setScaleTypeForImageView(ImageView.ScaleType scaletype) {
        mScaleType = scaletype;
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES * MIN_LOOPS;
    }

};