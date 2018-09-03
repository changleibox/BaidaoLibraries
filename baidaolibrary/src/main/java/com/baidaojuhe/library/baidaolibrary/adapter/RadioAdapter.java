/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.RadioViewHolder;
import com.baidaojuhe.library.baidaolibrary.listener.OnItemCheckedListener;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 单选
 */

public abstract class RadioAdapter<T, VH extends RadioViewHolder> extends ArrayAdapter<T, VH> {

    private int mSelectedPosition = -1;
    @Nullable
    private OnItemCheckedListener mCheckedListener;

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Nullable
    public T getSelectedItem() {
        return getItem(mSelectedPosition);
    }

    public void setSelectedItem(T item) {
        mSelectedPosition = getPosition(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(view -> {
            if (mSelectedPosition != -1) {
                notifyItemChanged(mSelectedPosition);
            }
            notifyItemChanged(mSelectedPosition = holder.getLayoutPosition());
            if (mCheckedListener != null) {
                mCheckedListener.onItemChecked(mSelectedPosition, true);
            }
        });
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.mCheckedListener = listener;
    }
}
