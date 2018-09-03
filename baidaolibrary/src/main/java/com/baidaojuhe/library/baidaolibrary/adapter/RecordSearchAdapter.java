/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.SearchViewHolder;
import com.baidaojuhe.library.baidaolibrary.entity.BDRecord;

import net.box.app.library.adapter.IArrayAdapter;

/**
 * Created by box on 2017/5/19.
 * <p>
 * 带有历史记录功能的搜索
 */

@SuppressWarnings("unchecked")
public abstract class RecordSearchAdapter<T extends BDRecord, VH extends SearchViewHolder> extends SearchAdapter<T, SearchViewHolder> {

    private static final int TYPE_RECORD = Integer.MIN_VALUE / 2;

    @Override
    public int getItemViewType(int position) {
        int itemViewType = super.getItemViewType(position);
        return isHistoryRecord() && getItemViewTypeEnum(itemViewType) == ItemType.Item ? TYPE_RECORD : itemViewType;
    }

    @Override
    protected final SearchViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_RECORD ? new RecordsViewHolder(parent) : onItemCreateViewHolder(parent, viewType);
    }

    @Override
    protected final void onBindItemViewHolder(SearchViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_RECORD) {
            holder.onBindDatas(this, position);
        } else {
            onItemBindViewHolder((VH) holder, position);
        }
    }

    protected abstract VH onItemCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onItemBindViewHolder(VH holder, int position);

    @SuppressWarnings("WeakerAccess")
    class RecordsViewHolder extends SearchViewHolder {

        private TextView tvName;

        public RecordsViewHolder(ViewGroup parent) {
            super(R.layout.bd_item_records, parent);
            tvName = findById(R.id.bd_tv_name);
        }

        @Override
        public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
            T record = getItem(position);
            if (record == null) {
                return;
            }
            tvName.setText(record.getRecordName());
        }
    }
}
