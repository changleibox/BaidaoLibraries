/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.helper.ThemeHelper;
import com.baidaojuhe.library.baidaolibrary.widget.IToolbar;

/**
 * Created by box on 2017/11/22.
 * <p>
 * 初始化actionbar
 */

public abstract class BDActionBarActivity extends BDBaseAppCompatActivity {

    @Nullable
    private IToolbar mToolbar;
    private ThemeHelper mThemeHelper;

    private boolean isModality;

    public BDActionBarActivity() {
        super();
        mThemeHelper = new ThemeHelper(this);
    }

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        isModality = getBundle().getBoolean(BDConstants.BDKey.KEY_IS_MODALITY, false);
        if (isModality) {
            setTheme(R.style.BD_AppTheme_Modality);
        }
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    public void setContentView(View contentView) {
        contentView = mThemeHelper.createContentView(contentView);
        super.setContentView(contentView);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, (ViewGroup) getWindow().getDecorView(), false));
    }

    @Override
    public void onContentChanged() {
        setSupportActionBar(mToolbar = mThemeHelper.getToolbar());
        super.onContentChanged();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isModality) {
            overridePendingTransition(R.anim.bd_anim_stay, R.anim.bd_anim_slide_out_to_bottom);
        }
    }

    @Nullable
    protected IToolbar getToolbar() {
        return mToolbar;
    }
}
