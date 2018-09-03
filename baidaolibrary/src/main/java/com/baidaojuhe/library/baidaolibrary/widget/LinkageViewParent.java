/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidaojuhe.library.baidaolibrary.R;

import net.box.app.library.util.ILogCompat;

/**
 * Created by box on 2017/11/30.
 * <p>
 * LinkageView 容器
 */

public class LinkageViewParent extends FrameLayout {

    private static final int DEFAULT_MAX_HEIGHT = 0;

    public LinkageViewParent(Context context) {
        super(context);
    }

    public LinkageViewParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkageViewParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LinkageViewParent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return checkLayoutParams(p) ? p : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        final int widthMode = MeasureSpec.getMode(widthSpec);
        final int heightMode = MeasureSpec.getMode(heightSpec);
        if (widthMode != MeasureSpec.AT_MOST) {
            ILogCompat.w("LinkageView", "onMeasure: widthSpec " + MeasureSpec.toString(widthSpec) + " should be AT_MOST");
        }
        if (heightMode != MeasureSpec.AT_MOST) {
            ILogCompat.w("LinkageView", "onMeasure: heightSpec " + MeasureSpec.toString(heightSpec) + " should be AT_MOST");
        }

        final int widthSize = MeasureSpec.getSize(widthSpec);
        final int heightSize = MeasureSpec.getSize(heightSpec);
        int maxWidth = widthSize;
        int maxHeight = heightSize;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (lp.maxHeight > 0 && lp.maxHeight < maxHeight) {
                maxHeight = lp.maxHeight;
            }
        }

        final int wPadding = getPaddingLeft() + getPaddingRight();
        final int hPadding = getPaddingTop() + getPaddingBottom();
        maxWidth -= wPadding;
        maxHeight -= hPadding;

        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : 0;
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int childWidthSpec = makeChildMeasureSpec(maxWidth, lp.width);
            final int childHeightSpec = makeChildMeasureSpec(maxHeight, lp.height);

            child.measure(childWidthSpec, childHeightSpec);

            width = Math.max(width, Math.min(child.getMeasuredWidth(), widthSize - wPadding));
            height = Math.max(height, Math.min(child.getMeasuredHeight(), heightSize - hPadding));
        }
        setMeasuredDimension(width + wPadding, height + hPadding);
    }

    private int makeChildMeasureSpec(int maxSize, int childDimen) {
        final int mode;
        final int size;
        switch (childDimen) {
            case LayoutParams.WRAP_CONTENT:
                mode = MeasureSpec.AT_MOST;
                size = maxSize;
                break;
            case LayoutParams.MATCH_PARENT:
                mode = MeasureSpec.EXACTLY;
                size = maxSize;
                break;
            default:
                mode = MeasureSpec.EXACTLY;
                size = Math.min(maxSize, childDimen);
                break;
        }
        return MeasureSpec.makeMeasureSpec(size, mode);
    }

    public class LayoutParams extends FrameLayout.LayoutParams {

        @ViewDebug.ExportedProperty(category = "layout")
        int maxHeight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LinkageViewParent_Layout, 0, 0);
            maxHeight = a.getDimensionPixelSize(R.styleable.LinkageViewParent_Layout_layout_maxHeight, DEFAULT_MAX_HEIGHT);
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LayoutParams other) {
            super(other);

            maxHeight = other.maxHeight;
        }
    }
}
