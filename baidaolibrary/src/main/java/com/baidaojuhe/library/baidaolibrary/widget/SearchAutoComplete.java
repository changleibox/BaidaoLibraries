/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.KeyEvent;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@SuppressWarnings("RestrictedApi")
@RestrictTo(LIBRARY_GROUP)
public class SearchAutoComplete extends SearchView.SearchAutoComplete {

    public SearchAutoComplete(Context context) {
        super(context);
    }

    public SearchAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    }
}