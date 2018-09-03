/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.GalleryWidget;

import android.view.ViewGroup;

import net.box.app.library.IContext;

import java.util.List;

import ru.truba.touchgallery.TouchView.FileTouchImageView;

public class FilePagerAdapter extends BasePagerAdapter {

    public FilePagerAdapter(IContext context, List<String> resources) {
        super(context, resources);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager) container).mCurrentView = ((FileTouchImageView) object).getImageView();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final FileTouchImageView iv = new FileTouchImageView(mContext);
        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        collection.addView(iv, 0);
        return iv;
    }

}
