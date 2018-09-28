/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDConfig;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import net.box.app.library.compat.IAttrCompat;
import net.box.app.library.widget.ISwipeRecyclerView;

/**
 * Created by box on 2017/8/24.
 * <p>
 * 具有上滑加载更多功能
 */

public class SwipeRecyclerView extends ISwipeRecyclerView {

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        BDUtils.setSuperPrivateFieldValue(this, "mCurrentPage", BDConfig.FIRST_PAGE);
    }

    @Override
    public void setCurrentPage(int page) {
        if (page < BDConfig.FIRST_PAGE) {
            page = BDConfig.FIRST_PAGE;
        }
        super.setCurrentPage(page);
    }

    @Override
    public void setResultSize(int resultSize) {
        super.setResultSize(resultSize);
        if (resultSize == FAILURE_RESULT) {
            int currentPage = getCurrentPage();
            setCurrentPage(currentPage - 1);
        }
    }

    public boolean isFirstPage() {
        return getCurrentPage() == BDConfig.FIRST_PAGE;
    }

    public void setFooterViewBackground(@ColorInt int color) {
        final View footerView = getFooterView(getContext());
        if (footerView != null) {
            footerView.setBackgroundColor(color);
        }
    }

    @Override
    protected View getFooterView(Context context) {
        View footerView = super.getFooterView(context);
        try {
            footerView.setBackgroundColor(IAttrCompat.getColor(context, android.R.attr.windowBackground, Color.TRANSPARENT));
        } catch (Exception e) {
            footerView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWindowBackground));
        }
        return footerView;
    }
}
