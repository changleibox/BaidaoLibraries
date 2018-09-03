/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.helper;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.activity.BDActionBarActivity;
import com.baidaojuhe.library.baidaolibrary.widget.IToolbar;

import net.box.app.library.compat.IAttrCompat;

/**
 * Created by box on 2017/11/22.
 * <p>
 * 处理主题
 */

public class ThemeHelper {

    private BDActionBarActivity mActivity;
    private IToolbar mToolbar;

    public ThemeHelper(BDActionBarActivity activity) {
        this.mActivity = activity;
    }

    public View createContentView(View contentView) {
        boolean immersiveEnable = IAttrCompat.getBoolean(mActivity, R.attr.immersiveEnable, false);
        if (!immersiveEnable) {
            mToolbar = contentView.findViewById(R.id.bd_toolbar);
            return contentView;
        }
        LinearLayout toolbarContentView = new LinearLayout(mActivity);
        toolbarContentView.setOrientation(LinearLayout.VERTICAL);

        mToolbar = (IToolbar) LayoutInflater.from(mActivity).inflate(R.layout.bd_layout_actionbar, (ViewGroup) mActivity.getWindow().getDecorView(), false);

        ViewGroup.LayoutParams dividerParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, IAttrCompat.getValue(mActivity, R.attr.actionBarElevation, 0));

        LinearLayout systemLayout = new LinearLayout(mActivity);
        systemLayout.setOrientation(LinearLayout.VERTICAL);

        View divider = new View(mActivity);
        divider.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorDividerNormal));

        systemLayout.addView(mToolbar);
        systemLayout.addView(divider, dividerParams);

        ViewGroup.LayoutParams systemBarParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toolbarContentView.addView(systemLayout, 0, systemBarParams);
        toolbarContentView.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return toolbarContentView;
    }

    public IToolbar getToolbar() {
        return mToolbar;
    }
}
