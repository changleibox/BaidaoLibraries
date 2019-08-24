/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.impl.Expandable;

import net.box.app.library.adapter.IArrayAdapter;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 可收缩的
 */

@SuppressWarnings({"unchecked", "WeakerAccess"})
public abstract class SearchExpandableViewHolder extends SearchViewHolder {

    private final ImageView mIvDropdown;
    private final AppCompatTextView mTvTitle;
    private final RecyclerView mRvSubitem;
    private final View mDivider;

    SearchExpandableViewHolder(@NonNull ViewGroup parent) {
        this(R.layout.bd_item_expandable, parent);
    }

    SearchExpandableViewHolder(@LayoutRes int id, @NonNull ViewGroup parent) {
        super(id, parent);
        mIvDropdown = findById(R.id.bd_iv_dropdown);
        mTvTitle = findById(R.id.bd_tv_title);
        mRvSubitem = findById(R.id.bd_rv_subitem);
        mDivider = findById(R.id.bd_divider);
    }

    @Override
    public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
        super.onBindDatas(adapter, position);
        if (!(adapter instanceof Expandable)) {
            return;
        }
        Expandable expandable = (Expandable) adapter;
        boolean isExpanded = expandable.isExpanded(position);
        mDivider.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        mIvDropdown.setSelected(isExpanded);

        ((View) mTvTitle.getParent()).setOnClickListener(createClickListener(adapter, position));

        mTvTitle.setText(getTitle(adapter, position));
        mRvSubitem.setAdapter(getAdapter(adapter, position));
        mRvSubitem.setNestedScrollingEnabled(false);
        mRvSubitem.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    public <T> T getItem(IArrayAdapter<?, ?> adapter, int position) {
        return (T) adapter.getItem(position);
    }

    protected abstract String getTitle(IArrayAdapter adapter, int position);

    protected abstract ArrayAdapter<?, ?> getAdapter(IArrayAdapter adapter, int position);

    protected View.OnClickListener createClickListener(@NonNull IArrayAdapter adapter, int position) {
        return v -> {
            ((Expandable) adapter).switchExpandedStatus(position);
            adapter.notifyItemChanged(position);
        };
    }
}
