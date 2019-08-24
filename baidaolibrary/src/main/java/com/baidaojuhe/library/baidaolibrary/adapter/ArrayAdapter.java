/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import androidx.annotation.NonNull;

import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;

import net.box.app.library.adapter.IArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by box on 2017/8/15.
 * <p>
 * 自定义Adapter
 */

public abstract class ArrayAdapter<T, VH extends BaseViewHolder> extends IArrayAdapter<T, VH> {

    public ArrayAdapter() {
        super();
    }

    public ArrayAdapter(@NonNull T[] objects) {
        super(objects);
    }

    public int getRealItemCount() {
        return getItemCount();
    }

    public void reverse() {
        List<T> items = new ArrayList<>(getItems());
        Collections.reverse(items);
        set(items);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.onBindDatas(this, position);
        holder.onBindDatas(position);
    }
}
