/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.AnswerViewHolder;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.entity.Answer;
import com.baidaojuhe.library.baidaolibrary.impl.Expandable;
import com.baidaojuhe.library.baidaolibrary.impl.OnAnswerSelectedImpl;
import com.baidaojuhe.library.baidaolibrary.presenter.ExpandablePresenter;

import net.box.app.library.adapter.IArrayAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by box on 2018/5/2.
 * <p>
 * 到访问卷答案选择
 */
@SuppressWarnings("WeakerAccess")
public class AnswerAdapter extends ArrayAdapter<Answer, BaseViewHolder> implements Expandable, OnAnswerSelectedImpl {

    private static final int TYPE_GROUP = 0;
    private static final int TYPE_CHILD = 1;

    private final boolean isSingleSelection;

    private ExpandablePresenter mPresenter;
    private OnAnswerSelectedImpl mSelectedImpl;

    public AnswerAdapter(@NonNull OnAnswerSelectedImpl activity, boolean isSingleSelection) {
        this.isSingleSelection = isSingleSelection;
        this.mSelectedImpl = activity;
        mPresenter = new ExpandablePresenter(this);
    }

    public void setSelected(Answer answer) {
        mSelectedImpl.setSelected(answer, !isSelected(answer));
    }

    @Override
    public boolean isSelected(Answer answer) {
        return mSelectedImpl.isSelected(answer);
    }

    @Override
    public void setSelected(Answer answer, boolean isChecked) {
        mSelectedImpl.setSelected(answer, isChecked);
    }

    @Override
    public List<Answer> getSelectedItems() {
        return mSelectedImpl.getSelectedItems();
    }

    @Override
    public void setSelectedItems(List<Answer> items) {
        mSelectedImpl.setSelectedItems(items);
    }

    @Override
    public int getItemViewType(int position) {
        Answer answer = getItem(position);
        List<Answer> childs = answer.getChilds();
        return childs == null || childs.isEmpty() ? TYPE_CHILD : TYPE_GROUP;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == TYPE_CHILD ? new AnswerViewHolder(parent, isSingleSelection) : new ExpandableViewHolder(parent);
    }

    @Override
    public void onContentChanged() {
        mPresenter.onContentChanged();
    }

    @Override
    public boolean isExpanded(int position) {
        return getItemViewType(position) != TYPE_CHILD && mPresenter.isExpanded(position);
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

    class ExpandableViewHolder extends BaseViewHolder {

        private ImageView mIvDropdown;
        private AppCompatTextView mTvTitle;
        private RecyclerView mRvSubitem;
        private View mDivider;

        ExpandableViewHolder(@NonNull ViewGroup parent) {
            super(R.layout.bd_item_expandable, parent);
            mIvDropdown = IViewCompat.findById(itemView, R.id.bd_iv_dropdown);
            mTvTitle = IViewCompat.findById(itemView, R.id.bd_tv_title);
            mRvSubitem = IViewCompat.findById(itemView, R.id.bd_rv_subitem);
            mDivider = IViewCompat.findById(itemView, R.id.bd_divider);
        }

        @Override
        public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
            super.onBindDatas(adapter, position);
            boolean isExpanded = isExpanded(position);
            mDivider.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            mIvDropdown.setSelected(isExpanded);

            ((View) mTvTitle.getParent()).setOnClickListener(v -> {
                switchExpandedStatus(position);
                adapter.notifyItemChanged(position);
            });

            Answer answer = getItem(position);

            AnswerAdapter answerAdapter = new AnswerAdapter(mSelectedImpl, isSingleSelection);
            answerAdapter.set(answer.getChilds());

            mTvTitle.setText(answer.getContent());
            mRvSubitem.setAdapter(answerAdapter);
            mRvSubitem.setNestedScrollingEnabled(false);
            mRvSubitem.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }
}
