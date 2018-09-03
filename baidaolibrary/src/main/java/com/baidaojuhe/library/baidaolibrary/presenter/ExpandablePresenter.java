/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.presenter;

import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.impl.Expandable;

/**
 * Created by box on 2017/11/1.
 * <p>
 * 可展开的
 */

public class ExpandablePresenter implements Expandable {

    private boolean[] mExpandeds;
    private ArrayAdapter mAdapter;

    public ExpandablePresenter(ArrayAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onContentChanged() {
        mExpandeds = new boolean[mAdapter.getItemCount()];
    }

    @Override
    public boolean isExpanded(int position) {
        return mExpandeds[position];
    }

    @Override
    public void setExpanded(int position) {
        mExpandeds[position] = true;
    }

    @Override
    public void setCollapsed(int position) {
        mExpandeds[position] = false;
    }

    @Override
    public void switchExpandedStatus(int position) {
        mExpandeds[position] = !mExpandeds[position];
        mAdapter.notifyItemChanged(position);
    }
}
