/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import net.box.app.library.widget.ISwipeRecyclerView;

/**
 * Created by box on 2017/5/16.
 * <p>
 * 加载更多的兼容
 */

public class SwipeRecyclerViewCompat extends ISwipeRecyclerView {

    private OnLoadMoreListener mLoadMoreListener;

    public SwipeRecyclerViewCompat(Context context) {
        this(context, null);
    }

    public SwipeRecyclerViewCompat(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerViewCompat(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        super.setOnLoadMoreListener(currentPage -> {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        });
    }

    @Override
    public void onScrolled(int dx, int dy) {
        setComplete(true);
        performLoadMore();
    }

    @Override
    public void setLoadMoreEnabled(boolean enable) {
        super.setLoadMoreEnabled(enable);
        if (!enable) {
            setComplete(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public final void setLoadMoreComplete(boolean complete) {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final void setTotal(@IntRange(from = NO_TOTAL) int total) {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final void setCurrentPage(int page) {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final int getCurrentPage() {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final int getPageSize() {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final void setPageSize(@IntRange(from = 1) int pageSize) {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final void setResultSize(@IntRange(from = FAILURE_RESULT) int resultSize) {
        throw new UnsupportedOperationException("Stub!");
    }

    @Override
    public final void setOnLoadMoreListener(ISwipeRecyclerView.OnLoadMoreListener listener) {
        throw new UnsupportedOperationException("请调用SwipeRecyclerViewCompat.setOnLoadMoreListener(SwipeRecyclerViewCompat.OnLoadMoreListener)");
    }

    @Override
    protected boolean isLoadedAll() {
        return true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
