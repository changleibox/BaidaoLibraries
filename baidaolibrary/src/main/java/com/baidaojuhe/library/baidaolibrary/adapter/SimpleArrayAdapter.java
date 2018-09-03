/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.helper.IAppHelper;

/**
 * Created by box on 2017/8/22.
 * <p>
 * 简单的adapter
 */

public class SimpleArrayAdapter extends ArrayAdapter<String, SimpleArrayAdapter.SimpleViewHolder> {

    private int mSelectedPosition = -1;

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(parent);
    }

    public void setSelectedPosition(int position) {
        if (mSelectedPosition >= 0 && mSelectedPosition < getItemCount()) {
            notifyItemChanged(mSelectedPosition);
        }
        this.mSelectedPosition = position;
        if (mSelectedPosition >= 0 && mSelectedPosition < getItemCount()) {
            notifyItemChanged(mSelectedPosition);
        }
    }

    class SimpleViewHolder extends BaseViewHolder {

        private AppCompatButton mBtnItem;
        private View mDividerBottom;

        SimpleViewHolder(@NonNull ViewGroup parent) {
            super(R.layout.bd_item_simple_array_adapter, parent);

            mBtnItem = findById(R.id.bd_btn_item);
            mDividerBottom = findById(R.id.bd_divider_bottom);
        }

        @Override
        public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
            super.onBindDatas(adapter, position);
            MarginLayoutParams params = (MarginLayoutParams) mDividerBottom.getLayoutParams();
            params.leftMargin = position == adapter.getItemCount() - 1 ? 0 : IAppHelper.getDimensionPixelSize(R.dimen.sizeMarginNormal);
            mDividerBottom.setLayoutParams(params);

            mBtnItem.setText(getItem(position));
            mBtnItem.setSelected(mSelectedPosition == position);
            mBtnItem.setOnClickListener(view -> itemView.performClick());
            mBtnItem.setOnLongClickListener(view -> itemView.performLongClick());
        }
    }
}
