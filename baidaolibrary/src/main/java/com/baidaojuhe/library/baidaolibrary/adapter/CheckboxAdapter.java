/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.listener.OnItemCheckedListener;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 多选
 */

public abstract class CheckboxAdapter<T, VH extends BaseViewHolder> extends ArrayAdapter<T, VH> {

    private final Set<Integer> mSelectedPositions = new ArraySet<>();
    @Nullable
    private OnItemCheckedListener mCheckedListener;

    public boolean isSelected(int position) {
        return mSelectedPositions.contains(position);
    }

    public void setSelected(int position, boolean isChecked) {
        Integer value = position;
        mSelectedPositions.remove(value);
        if (isChecked) {
            mSelectedPositions.add(position);
        }
        if (mCheckedListener != null) {
            mCheckedListener.onItemChecked(position, isChecked);
        }
    }

    public List<T> getSelectedItems() {
        return Stream.of(mSelectedPositions).map(this::getItem)
                .filter(BDUtils::nonNull).collect(Collectors.toList());
    }

    public void setSelectedItems(List<T> items) {
        mSelectedPositions.clear();
        if (items != null) {
            mSelectedPositions.addAll(Stream.of(items).map(this::getPosition)
                    .filter(position -> position != -1).collect(Collectors.toList()));
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(view -> {
            setSelected(position, !isSelected(position));
            notifyItemChanged(position);
        });
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.mCheckedListener = listener;
    }
}
