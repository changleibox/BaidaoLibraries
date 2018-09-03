/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.R;

import net.box.app.library.compat.IAttrCompat;

/**
 * Created by box on 2017/3/22.
 * <p>
 * 自定义ToolBar，主要是为了把title显示在中间
 */

public class IToolbar extends Toolbar {

    private int mPaddingTitle;

    @Nullable
    private AppCompatTextView mTvTitle;
    @Nullable
    private AppCompatTextView mTvSubtitleTitle;

    private int mTitleMargin = -1;
    private int mTitleImageResId;
    private View mCustomTitleView;

    private CharSequence mTitleText;
    private CharSequence mSubtitleText;

    public IToolbar(Context context) {
        super(context);
        resetTitle();
    }

    public IToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resetTitle();
    }

    public IToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resetTitle();
    }

    public void resetTitle() {
        mTitleImageResId = 0;
        mPaddingTitle = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public CharSequence getSubtitle() {
        return mSubtitleText;
    }

    @Nullable
    public View setCustomTitleView(@LayoutRes int resId) {
        return setCustomTitleView(inflate(getContext(), resId, null));
    }

    @Nullable
    public View setCustomTitleView(View customTitleView) {
        return setCustomTitleView(customTitleView, generateDefaultLayoutParams());
    }

    @Nullable
    public View setCustomTitleView(@LayoutRes int resId, int gravity) {
        return setCustomTitleView(inflate(getContext(), resId, null), new LayoutParams(gravity));
    }

    @Nullable
    public View setCustomTitleView(View customTitleView, int gravity) {
        return setCustomTitleView(customTitleView, new LayoutParams(gravity));
    }

    @Nullable
    public View setCustomTitleView(@LayoutRes int resId, LayoutParams params) {
        return setCustomTitleView(inflate(getContext(), resId, null), params);
    }

    @Nullable
    public View setCustomTitleView(View customTitleView, LayoutParams params) {
        setTitle(null);
        setSubtitle(null);
        if (mCustomTitleView != null) {
            removeView(mCustomTitleView);
        }
        if (customTitleView != null) {
            addView(customTitleView, params);
        }
        return mCustomTitleView = customTitleView;
    }

    public void setTitleImage(@DrawableRes int titleResId) {
        if (mCustomTitleView != null) {
            return;
        }
        if (titleResId != 0) {
            if (mTvTitle == null) {
                mTvTitle = new AppCompatTextView(getContext());
                mTvTitle.setSingleLine();
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setTextAppearance(getContext(), IAttrCompat.getResourceId(getContext(), R.attr.titleTextStyle, 0));
                mTvTitle.setPadding(mPaddingTitle, 0, mPaddingTitle, 0);
                LayoutParams layoutParams = generateDefaultLayoutParams();
                layoutParams.gravity = Gravity.CENTER;
                addView(mTvTitle, layoutParams);
            }
        } else if (mTvTitle != null) {
            removeView(mTvTitle);
            mTvTitle = null;
        }
        if (mTvTitle != null) {
            mTvTitle.setText(null);
            mTvTitle.setBackgroundResource(titleResId);
        }
        mTitleImageResId = titleResId;
    }

    public void setTitleTextAppearance(@StyleRes int textAppearance) {
        if (mTvTitle != null) {
            mTvTitle.setTextAppearance(getContext(), textAppearance);
        }
    }

    /**
     * Set the title of this toolbar.
     * <p>
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param title Title to set
     */
    @Override
    public void setTitle(CharSequence title) {
        if (mCustomTitleView != null) {
            return;
        }
        if (mTitleImageResId != 0) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            if (mTvTitle == null) {
                mTvTitle = new AppCompatTextView(getContext());
                mTvTitle.setSingleLine();
                mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvTitle.setTextAppearance(getContext(), IAttrCompat.getResourceId(getContext(), R.attr.titleTextStyle, 0));
                mTvTitle.setPadding(mPaddingTitle, 0, mPaddingTitle, 0);
                LayoutParams layoutParams = generateDefaultLayoutParams();
                layoutParams.gravity = Gravity.CENTER;
                addView(mTvTitle, layoutParams);
            }
        } else if (mTvTitle != null) {
            removeView(mTvTitle);
            mTvTitle = null;
        }
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            mTvTitle.setBackgroundResource(0);
        }
        mTitleText = title;
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        if (mCustomTitleView != null) {
            return;
        }
        if (!TextUtils.isEmpty(subtitle)) {
            if (mTvSubtitleTitle == null) {
                mTvSubtitleTitle = new AppCompatTextView(getContext());
                mTvSubtitleTitle.setSingleLine();
                mTvSubtitleTitle.setEllipsize(TextUtils.TruncateAt.END);
                mTvSubtitleTitle.setTextAppearance(getContext(), IAttrCompat.getResourceId(getContext(), R.attr.subtitleTextStyle, 0));
                mTvSubtitleTitle.setPadding(mPaddingTitle, 0, mPaddingTitle, 0);
                LayoutParams layoutParams = generateDefaultLayoutParams();
                // layoutParams.gravity = Gravity.CENTER;
                addView(mTvSubtitleTitle, layoutParams);
            }
        } else if (mTvSubtitleTitle != null) {
            removeView(mTvSubtitleTitle);
            mTvSubtitleTitle = null;
        }
        if (mTvSubtitleTitle != null) {
            mTvSubtitleTitle.setText(subtitle);
        }
        mSubtitleText = subtitle;
    }

    public void setTitleTextColor(@ColorInt int color) {
        if (mTvTitle != null) {
            mTvTitle.setTextColor(color);
        }
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        if (mTvSubtitleTitle != null) {
            mTvSubtitleTitle.setTextColor(color);
        }
    }

    public int getTitleTextColor() {
        if (mTvTitle != null) {
            return mTvTitle.getCurrentTextColor();
        }
        return 0;
    }

    public int getSubtitleTextColor() {
        if (mTvSubtitleTitle != null) {
            return mTvSubtitleTitle.getCurrentTextColor();
        }
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        refreshSubtitleLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        refreshSubtitleLayout();
    }

    private void refreshSubtitleLayout() {
        if (mTvSubtitleTitle == null) {
            return;
        }
        if (mTitleMargin == -1) {
            mTitleMargin = IAttrCompat.getValue(getContext(), R.attr.titleMargin, 0);
        }
        if (mTvTitle != null) {
            int titleWidth = mTvTitle.getWidth();
            int titleHeight = mTvTitle.getHeight();
            float titleX = mTvTitle.getX();
            float titleY = mTvTitle.getY();
            mTvSubtitleTitle.setX(titleX + titleWidth + mTitleMargin);
            mTvSubtitleTitle.setY(titleY + titleHeight - mTvSubtitleTitle.getHeight());
        } else {
            LayoutParams layoutParams = (LayoutParams) mTvSubtitleTitle.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            mTvSubtitleTitle.setLayoutParams(layoutParams);
        }
    }

}
