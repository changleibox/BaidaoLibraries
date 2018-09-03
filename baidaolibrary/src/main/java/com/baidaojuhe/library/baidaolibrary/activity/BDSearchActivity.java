/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.SearchAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.SearchViewHolder;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.impl.OnItemClickListener;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.widget.SwipeRecyclerView;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.widget.ISwipeRecyclerView;

import java.util.List;

/**
 * Created by box on 2017/3/29.
 * <p>
 * 搜索Activity
 */

@SuppressWarnings("unused")
public abstract class BDSearchActivity extends BDActionBarActivity implements OnQueryTextListener, OnItemClickListener {

    private static final int FIRST_PAGE = 1;

    private SearchView mSearchView;
    private SwipeRecyclerView mRvSearchContent;

    @Nullable
    private SearchAdapter mAdapter;

    private CharSequence mQuery;
    private boolean isLoadMoreEnabled = true;

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        setTheme(R.style.BD_AppTheme_Search);
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        mSearchView = IViewCompat.findById(this, R.id.bd_searchview);
        mRvSearchContent = IViewCompat.findById(this, R.id.bd_rv_search_content);
        Button btnCancel = IViewCompat.findById(this, R.id.bd_btn_cancel);

        ViewCompat.setTransitionName(mSearchView, BDConstants.BDTransitionName.TRANSITION_NAME_SEARCH);

        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);

        mRvSearchContent.setLayoutManager(new LinearLayoutManager(this));
        mRvSearchContent.setOnLoadMoreListener(currentPage -> {
            if (mAdapter != null && !TextUtils.isEmpty(mQuery)) {
                mAdapter.filter(mQuery, currentPage);
            } else {
                mRvSearchContent.setResultSize(ISwipeRecyclerView.FAILURE_RESULT);
            }
        });

        btnCancel.setOnClickListener(v -> onClickCancel());

        super.onContentChanged();
    }

    @Override
    public final BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_search);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRvSearchContent.addItemDecoration(decoration);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRvSearchContent.removeItemDecoration(decoration);
    }

    public void setQueryText(CharSequence text) {
        setQueryText(text, true);
    }

    @SuppressWarnings("SameParameterValue")
    public void setQueryText(CharSequence text, boolean submit) {
        mSearchView.setQuery(text, submit);
    }

    public CharSequence getQueryText() {
        return mSearchView.getQuery();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        initializationLoadMore();
        mQuery = mSearchView.getQuery();
        mRvSearchContent.setFooterVisibility(true);
        if (mAdapter != null) {
            mAdapter.filter(query, getCurrentPage());
        }
        return true;
    }

    @CallSuper
    @Override
    public boolean onQueryTextChange(String newText) {
        // mBtnCancel.setText(TextUtils.isEmpty(newText) ? R.string.btn_cancel : R.string.btn_search);
        // mBtnCancel.setBackgroundResource(TextUtils.isEmpty(newText) ? 0 : R.drawable.selector_radius_button);
        // mBtnCancel.setTextColor(TextUtils.isEmpty(newText) ? AppHelper.getColor(colorTextSearchButton) : Color.WHITE);
        if (TextUtils.isEmpty(newText)) {
            mQuery = null;
            if (mAdapter != null) {
                mAdapter.setNotifyOnChange(false);
                mAdapter.clear();
                mAdapter.setHistoryRecord(true);
                mAdapter.notifyDataSetChanged();
            }
            initializationLoadMore();
            mRvSearchContent.setFooterVisibility(false);
            onLoadHistoryRecords();
        }
        mRvSearchContent.setLoadMoreEnabled(isLoadMoreEnabled && !TextUtils.isEmpty(newText));
        IAppUtils.sendKeyCode(KeyEvent.KEYCODE_ENTER);
        return true;
    }

    public <T, VH extends SearchViewHolder> void setAdapter(@Nullable SearchAdapter<T, VH> adapter) {
        mAdapter = adapter;
        mRvSearchContent.setAdapter(adapter);
        if (adapter != null) {
            adapter.setOnItemClickListener(this);
        }
        onQueryTextChange(null);
    }

    public void setQueryHint(@StringRes int hint) {
        setQueryHint(getText(hint));
    }

    public void setQueryHint(CharSequence hint) {
        mSearchView.setQueryHint(hint);
    }

    public void initializationLoadMore() {
        mRvSearchContent.setComplete(false);
        mRvSearchContent.setCurrentPage(FIRST_PAGE);
        if (mAdapter != null) {
            mAdapter.setCurrentPage(FIRST_PAGE);
        }
    }

    public void setPage(int page) {
        mRvSearchContent.setCurrentPage(page);
    }

    public void setPageSize(int pageSize) {
        mRvSearchContent.setPageSize(pageSize);
    }

    /**
     * 获取更多完成，通知recyclerView刷新界面
     *
     * @param count -1为失败，大于-1则成功
     */
    public void setLoadMoreComplete(@IntRange(from = -1) int count) {
        mRvSearchContent.setResultSize(count);
    }

    public void setLoadMoreEnabled(boolean enable) {
        isLoadMoreEnabled = enable;
        mRvSearchContent.setLoadMoreEnabled(enable);
    }

    public int getCurrentPage() {
        return mRvSearchContent.getCurrentPage();
    }

    public void publishResults(@Nullable List<?> results, int currentPage) {
        if (mAdapter == null || TextUtils.isEmpty(mSearchView.getQuery())) {
            return;
        }
        mAdapter.setHistoryRecord(false);
        int resultSize = ISwipeRecyclerView.FAILURE_RESULT;
        if (currentPage == FIRST_PAGE && results == null) {
            mAdapter.clear();
        } else if (results != null) {
            if (currentPage == FIRST_PAGE) {
                //noinspection unchecked
                mAdapter.set(results);
            } else {
                //noinspection unchecked
                mAdapter.addAll(results);
            }
            resultSize = results.size();
        }
        setLoadMoreComplete(resultSize);
    }

    @Override
    public void onItemClick(IArrayAdapter parent, View view, int position, long id) {
    }

    public void onLoadHistoryRecords() {
    }

    public void onClickCancel() {
        hideKeyboard();
        // if (TextUtils.isEmpty(mSearchView.getQuery())) {
        onBackPressed();
        // } else {
        //     setQueryText(mSearchView.getQuery(), true);
        // }
    }

    public void publishResults(@Nullable CharSequence constraint, @Nullable List<Void> results, int currentPage) {
        publishResults(results, currentPage);
    }

}
