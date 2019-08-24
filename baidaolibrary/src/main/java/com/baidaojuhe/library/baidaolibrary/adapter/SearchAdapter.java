/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.SearchViewHolder;
import com.baidaojuhe.library.baidaolibrary.impl.OnItemClickListener;
import com.baidaojuhe.library.baidaolibrary.impl.OnItemLongClickListener;

import net.box.app.library.helper.IAppHelper;

import java.util.List;

/**
 * Created by box on 2017/3/30.
 * <p>
 * 搜索
 */

@SuppressWarnings({"WeakerAccess", "unchecked"})
public abstract class SearchAdapter<T, VH extends SearchViewHolder> extends ArrayAdapter<T, BaseViewHolder> {

    private boolean isHistoryRecord;

    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private FilterFactroy<T> mFactroy;
    private Filter mFilter;
    private int mCurrentPage;

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return isHistoryRecord ? itemCount + 2 : itemCount;
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (getItemViewTypeEnum(viewType)) {
            case Header:
                return new HeaderViewHolder(parent);
            case Item:
                return onCreateItemViewHolder(parent, viewType);
            case Clean:
                return new FooterViewHolder(parent);
        }
        return null;
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.onBindDatas(position);
        switch (getItemViewTypeEnum(holder.getItemViewType())) {
            case Item:
                int adapterPosition = ((VH) holder).setHistoryRecord(isHistoryRecord).getRealPosition();
                if (mClickListener != null) {
                    holder.itemView.setOnClickListener(v -> mClickListener.onItemClick(SearchAdapter.this, v, adapterPosition, getItemId(adapterPosition)));
                } else {
                    holder.itemView.setOnClickListener(null);
                }
                if (mLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(v -> mLongClickListener.onItemLongClick(SearchAdapter.this, v, adapterPosition, getItemId(adapterPosition)));
                } else {
                    holder.itemView.setOnLongClickListener(null);
                }
                onBindItemViewHolder((VH) holder, adapterPosition);
                break;
            default:
                holder.itemView.setClickable(false);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isHistoryRecord ? (position == 0 ? ItemType.Header : position == getItemCount() - 1 ? ItemType.Clean : ItemType.Item) : ItemType.Item).ordinal();
    }

    public ItemType getItemViewTypeEnum(int viewType) {
        return viewType == ItemType.Header.ordinal() ? ItemType.Header : viewType == ItemType.Clean.ordinal() ? ItemType.Clean : ItemType.Item;
    }

    public boolean isHistoryRecord() {
        return isHistoryRecord;
    }

    public void setHistoryRecord(boolean historyRecord) {
        isHistoryRecord = historyRecord;
        notifyDataSetChanged();
    }

    public enum ItemType {
        Header, Item, Clean
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongClickListener = listener;
        notifyDataSetChanged();
    }

    public void setFilterFactroy(FilterFactroy<T> factroy) {
        this.mFactroy = factroy;
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    public final void filter(CharSequence text, int currentPage) {
        this.mCurrentPage = currentPage;
        getFilter().filter(text);
    }

    @NonNull
    @Override
    public final Filter getFilter() {
        return mFactroy == null ? super.getFilter() : mFilter != null ? mFilter : (mFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                List<?> objects = mFactroy.performFiltering(constraint, mCurrentPage);
                if (objects != null) {
                    results.count = objects.size();
                    results.values = objects;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFactroy.publishResults(constraint, (List<T>) results.values, mCurrentPage);
            }
        });
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindItemViewHolder(VH holder, int position);

    protected String getHeaderText() {
        return IAppHelper.getString(R.string.bd_label_history_record);
    }

    protected void onClickFooter(View v) {
        clear();
    }

    class HeaderViewHolder extends BaseViewHolder {

        private TextView tvName;

        HeaderViewHolder(ViewGroup parent) {
            super(R.layout.bd_item_search_header, parent);

            tvName = findById(R.id.bd_tv_name);
        }

        @Override
        public void onBindDatas(int position) {
            tvName.setText(getHeaderText());
        }
    }

    class FooterViewHolder extends BaseViewHolder {

        private Button btnClean;

        FooterViewHolder(ViewGroup parent) {
            super(R.layout.bd_item_search_clean, parent);

            btnClean = findById(R.id.bd_btn_clean);
        }

        @Override
        public void onBindDatas(int position) {
            btnClean.setOnClickListener(SearchAdapter.this::onClickFooter);
        }
    }

    public interface FilterFactroy<T> {
        @WorkerThread
        @Nullable
        List<T> performFiltering(@Nullable CharSequence constraint, int currentPage);

        @MainThread
        void publishResults(@Nullable CharSequence constraint, @Nullable List<T> results, int currentPage);
    }
}
