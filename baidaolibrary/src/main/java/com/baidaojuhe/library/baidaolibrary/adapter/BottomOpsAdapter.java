/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BottomOpsViewHolder;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 底部有操作按钮的对话框
 */

public class BottomOpsAdapter extends ArrayAdapter<String, BottomOpsViewHolder> {

    @Override
    public BottomOpsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BottomOpsViewHolder(R.layout.bd_item_bottom_ops, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomOpsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        holder.tvOperation.setText(getItem(position));
    }
}
