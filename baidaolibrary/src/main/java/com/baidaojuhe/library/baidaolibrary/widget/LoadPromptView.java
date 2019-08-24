/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.impl.LoadPromptImpl;

import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IViewDrawable;

import me.box.retrofit.exception.HttpException;

/**
 * 显示加载提示
 */
public class LoadPromptView extends FrameLayout implements LoadPromptImpl {

    private TextView mTvPrompt;

    public LoadPromptView(Context context) {
        super(context);
        init(null, 0);
    }

    public LoadPromptView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoadPromptView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadPromptView, defStyle, 0);

        String promptText = a.getString(R.styleable.LoadPromptView_text);
        Drawable promptDrawable = null;
        int mode = a.getInt(R.styleable.LoadPromptView_mode, MODE_NO_DATA);

        if (a.hasValue(R.styleable.LoadPromptView_drawable)) {
            promptDrawable = a.getDrawable(R.styleable.LoadPromptView_drawable);
            if (promptDrawable != null) {
                promptDrawable.setCallback(this);
            }
        }

        a.recycle();

        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.bd_widget_load_prompt, this);

        mTvPrompt = findViewById(R.id.bd_tv_message);

        setPromptText(promptText);
        setPromptDrawable(promptDrawable);
        setMode(mode);

        if (isInEditMode()) {
            setSuccess();
        }
    }

    @Override
    public void setPromptText(CharSequence text) {
        setVisibility(VISIBLE);
        mTvPrompt.setText(text);
    }

    @Override
    public void setPromptText(@StringRes int text) {
        setPromptText(getContext().getText(text));
    }

    @Override
    public void setPromptDrawable(Drawable drawable) {
        setVisibility(VISIBLE);
        mTvPrompt.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    public void setPromptDrawable(@DrawableRes int drawable) {
        setPromptDrawable(IViewDrawable.getDrawable(getContext(), drawable));
    }

    @Override
    public void setMode(@Mode int mode) {
        switch (mode) {
            case MODE_SUCCESS:
                setSuccess();
                break;
            case MODE_LOAD_FAILURE:
                if (!IAppUtils.isNetAvailable(getContext())) {
                    setMode(MODE_NO_NETWORK);
                    break;
                }
                setPromptText(R.string.bd_prompt_load_failure);
                setPromptDrawable(R.drawable.bd_ic_load_failure);
                break;
            case MODE_NO_DATA:
                if (!IAppUtils.isNetAvailable(getContext())) {
                    setMode(MODE_NO_NETWORK);
                    break;
                }
                setPromptText(R.string.bd_prompt_no_data);
                setPromptDrawable(R.drawable.bd_ic_no_data);
                break;
            case MODE_NO_NETWORK:
                setPromptText(R.string.bd_prompt_network_error);
                setPromptDrawable(R.drawable.bd_ic_network_error);
                break;
        }
    }

    @Override
    public void setSuccess() {
        setVisibility(GONE);
    }

    @Override
    public void setError(Throwable e, boolean isFirstPage) {
        if (!isFirstPage) {
            return;
        }
        if (HttpException.isNotData(e)) {
            setMode(LoadPromptView.MODE_NO_DATA);
        } else if (HttpException.isNetworkError(e)) {
            setMode(LoadPromptView.MODE_NO_NETWORK);
        } else {
            setMode(LoadPromptView.MODE_LOAD_FAILURE);
        }
    }
}
