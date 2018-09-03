/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package ru.truba.touchgallery.TouchView;

import android.annotation.SuppressLint;
import android.util.AttributeSet;

import net.box.app.library.IContext;

@SuppressLint("ViewConstructor")
@SuppressWarnings("unused")
public class FileTouchImageView extends UrlTouchImageView {

    public FileTouchImageView(IContext ctx) {
        super(ctx);
    }

    public FileTouchImageView(IContext ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public void setUrl(String imagePath) {
        new ImageLoadTask().execute(imagePath);
    }

}
