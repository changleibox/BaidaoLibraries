/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 底部有操作按钮的对话框
 */

public class BottomOpsViewHolder extends BaseViewHolder {

    public View divider;
    public TextView tvOperation;

    public BottomOpsViewHolder(@LayoutRes int id, @NonNull ViewGroup parent) {
        super(id, parent);

        divider = findById(R.id.bd_divider);
        tvOperation = findById(R.id.bd_btn_operation);
    }
}
