/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.impl.Expandable;
import com.baidaojuhe.library.baidaolibrary.presenter.ExpandablePresenter;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 可收缩的
 */

public abstract class ExpandableAdapter<T, VH extends BaseViewHolder> extends ArrayAdapter<T, VH> implements Expandable {

    private ExpandablePresenter mPresenter;

    public ExpandableAdapter() {
        mPresenter = new ExpandablePresenter(this);
    }

    @Override
    public void onContentChanged() {
        mPresenter.onContentChanged();
    }

    @Override
    public boolean isExpanded(int position) {
        return mPresenter.isExpanded(position);
    }

    @Override
    public void setExpanded(int position) {
        mPresenter.setExpanded(position);
    }

    @Override
    public void setCollapsed(int position) {
        mPresenter.setCollapsed(position);
    }

    @Override
    public void switchExpandedStatus(int position) {
        mPresenter.switchExpandedStatus(position);
    }
}
