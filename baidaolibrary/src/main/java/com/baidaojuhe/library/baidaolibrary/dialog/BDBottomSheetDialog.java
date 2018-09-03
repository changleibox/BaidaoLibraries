/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.baidaojuhe.library.baidaolibrary.R;

import butterknife.ButterKnife;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 从底部弹出的dialog
 */

@SuppressWarnings("WeakerAccess")
public class BDBottomSheetDialog extends BottomSheetDialog {

    protected final Context context;

    public BDBottomSheetDialog(Context context) {
        this(context, R.style.BD_AppTheme_Dialog_Slide_FromBottom);
    }

    protected BDBottomSheetDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    public void show() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams;
        if (window != null) {
            layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }
        super.show();
    }

    @Override
    public void onContentChanged() {
        ButterKnife.bind(this);
    }
}
