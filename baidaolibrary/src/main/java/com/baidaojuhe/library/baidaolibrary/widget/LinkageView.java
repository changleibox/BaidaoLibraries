/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.entity.Linkage;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.helper.IScrollHelper;
import net.box.app.library.listener.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/8/23.
 * <p>
 * n级联动
 */

@SuppressWarnings("unused")
public class LinkageView extends LinearLayoutCompat {

    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;

    @IntDef({LEVEL_1, LEVEL_2, LEVEL_3})
    public @interface Level {
    }

    private static final int DEFAULT_MAX_LEVEL = 1;

    private final int mLinkageWidth;
    private final int mLinkageBackground;
    private final ContextThemeWrapper mThemeWrapper;

    private int mMaxLevel;
    private List<LinkageAdapter> mAdapters;

    private OnSelectedListener mSelectedListener;

    public LinkageView(@NonNull Context context) {
        this(context, null);
    }

    public LinkageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(LinearLayout.HORIZONTAL);
        super.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        super.setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.bd_shape_divider_vertical));

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinkageView, defStyleAttr, 0);
        int maxLevel = a.getInteger(R.styleable.LinkageView_maxLevel, DEFAULT_MAX_LEVEL);
        a.recycle();

        mLinkageWidth = getResources().getDimensionPixelSize(R.dimen.sizeLinkage);
        mLinkageBackground = ContextCompat.getColor(context, R.color.colorLinkageBackground);
        mThemeWrapper = new ContextThemeWrapper(context, R.style.BD_AppTheme_RecyclerView);

        setMaxLevel(maxLevel);
    }

    public void setMaxLevel(@Level int maxLevel) {
        this.mMaxLevel = maxLevel;
        this.mAdapters = new ArrayList<>(maxLevel);

        removeAllViews();
        Stream.iterate(0, i -> ++i).limit(maxLevel).forEach(this::addRecyclerViewToContainer);

        if (isInEditMode()) {
            setLinkages(getPreviewLinkages(maxLevel));
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setOnSelectedListener(OnSelectedListener l) {
        this.mSelectedListener = l;
    }

    public void performSelected() {
        performSelected(getSelectedPositions());
    }

    public void performSelected(Integer[] selectedPositions) {
        if (selectedPositions == null) {
            selectedPositions = getSelectedPositions();
        }
        if (mSelectedListener != null) {
            Linkage[] selectedLinkages = getLinkages(selectedPositions);
            long count = Stream.of(selectedLinkages).filter(BDUtils::nonNull).count();
            mSelectedListener.onSelected(this, selectedPositions, count == 0 ? null : selectedLinkages[(int) count - 1]);
        }
    }

    public void setItemSelected(Integer[] selectedPositions) {
        if (selectedPositions == null) {
            selectedPositions = getSelectedPositions();
        }

        int size = mAdapters.size();
        for (int i = 0; i < selectedPositions.length; i++) {
            if (i >= size) {
                continue;
            }
            Integer selectedPosition = selectedPositions[i];
            LinkageAdapter linkageAdapter = mAdapters.get(i);
            linkageAdapter.setSelectedPosition(selectedPosition < -1 ? -1 : selectedPosition);
            linkageAdapter.notifyDataSetChanged();
        }
    }

    public void setLinkages(List<Linkage> linkages) {
        Stream.of(mAdapters).forEach(linkageAdapter -> {
            int level = linkageAdapter.getLevel();
            if (level == 0 && linkages != null) {
                linkageAdapter.set(linkages);
            }
            setItemSelected(linkageAdapter, level, 0);
            linkageAdapter.setOnSelectedListener((parent, view, position, id) -> setItemSelected((LinkageAdapter) parent, level, position));
        });
    }

    public Linkage[] getLinkages(@NonNull Integer[] positions) {
        if (positions.length > mMaxLevel) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return Stream.range(0, positions.length).map(index -> mAdapters.get(index).getItem(positions[index])).toArray(Linkage[]::new);
    }

    public Integer[] getSelectedPositions() {
        return Stream.of(mAdapters).map(LinkageAdapter::getSelectedPosition).toArray(Integer[]::new);
    }

    public Linkage[] getSelectedLinkages() {
        return getLinkages(getSelectedPositions());
    }

    private RecyclerView addRecyclerViewToContainer(int level) {
        RecyclerView rvContent = new RecyclerView(mThemeWrapper);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.width = level < mMaxLevel - 1 ? mLinkageWidth : LayoutParams.MATCH_PARENT;
        rvContent.setBackgroundColor(level == 0 ? Color.WHITE : mLinkageBackground);
        addView(rvContent, params);

        LinkageAdapter linkageAdapter = new LinkageAdapter(level);
        rvContent.setAdapter(linkageAdapter);
        mAdapters.add(linkageAdapter);
        return rvContent;
    }

    private void setItemSelected(LinkageAdapter adapter, int level, int position) {
        int nextLevel = level + 1;
        RecyclerView recyclerView = (RecyclerView) getChildAt(level);
        RecyclerView nextRecyclerView = (RecyclerView) getChildAt(nextLevel);
        Linkage item = adapter.getItem(position);
        if (recyclerView != null && item != null) {
            new IScrollHelper(recyclerView).moveToPosition(position);
        }
        if (nextLevel >= mAdapters.size()) {
            performSelected();
            return;
        }
        if (item == null || recyclerView == null || nextRecyclerView == null) {
            return;
        }
        List<Linkage> nextLinkages = item.getLinkages();
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        LinkageAdapter nextLinkageAdapter = mAdapters.get(nextLevel);
        nextLinkageAdapter.setNotifyOnChange(false);
        if (nextLinkages == null || nextLinkages.isEmpty()) {
            nextRecyclerView.setVisibility(View.GONE);
            layoutParams.width = LayoutParams.MATCH_PARENT;
            nextLinkageAdapter.clear();
            nextLinkageAdapter.setSelectedPosition(-1);
        } else {
            nextRecyclerView.setVisibility(View.VISIBLE);
            layoutParams.width = mLinkageWidth;
            nextLinkageAdapter.set(nextLinkages);
            nextLinkageAdapter.setSelectedPosition(0);
        }
        nextLinkageAdapter.notifyDataSetChanged();
        recyclerView.setLayoutParams(layoutParams);
    }

    private class LinkageAdapter extends ArrayAdapter<Linkage, ViewHolder> implements IOnItemClickListener {

        private int mLevel;
        private int mSelectedPosition;
        private IOnItemClickListener mItemSelectedListener;

        private LinkageAdapter(int level) {
            this.mLevel = level;
            setOnItemClickListener(this);
        }

        int getLevel() {
            return mLevel;
        }

        int getSelectedPosition() {
            return mSelectedPosition;
        }

        void setSelectedPosition(int selectedPosition) {
            this.mSelectedPosition = selectedPosition;
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onItemClick(this, null, selectedPosition, 0);
            }
        }

        void setOnSelectedListener(IOnItemClickListener l) {
            this.mItemSelectedListener = l;
        }

        @SuppressWarnings("unused")
        public Linkage getSelectedItem() {
            return getItem(mSelectedPosition);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            Linkage linkage = getItem(position);
            holder.tvName.setText(linkage.getName());
            holder.tvName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.bd_selector_simple_text_color));
            holder.divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
            holder.itemView.setSelected(mSelectedPosition == position);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public void onItemClick(IArrayAdapter parent, View view, int position, long id) {
            notifyItemChanged(mSelectedPosition);
            setSelectedPosition(position);
            notifyItemChanged(position);
            int nextLevel = mLevel + 1;
            if (mAdapters.size() <= nextLevel || mAdapters.get(nextLevel).getItemCount() == 0) {
                Integer[] selectedPositions = getSelectedPositions();
                if (mSelectedListener != null) {
                    Linkage[] selectedLinkages = getLinkages(selectedPositions);
                    long count = Stream.of(selectedLinkages).filter(BDUtils::nonNull).count();
                    mSelectedListener.onLastSelected(LinkageView.this, selectedPositions, count == 0 ? null : selectedLinkages[(int) count - 1]);
                }
            }
        }
    }

    class ViewHolder extends BaseViewHolder {

        private TextView tvName;
        private View divider;

        ViewHolder(@NonNull ViewGroup parent) {
            super(R.layout.bd_widget_item_linkageview, parent);

            tvName = IViewCompat.findById(itemView, R.id.bd_tv_name);
            divider = IViewCompat.findById(itemView, R.id.bd_divider);
        }
    }

    public interface OnSelectedListener {
        void onSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out);

        void onLastSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out);
    }

    private List<Linkage> getPreviewLinkages(int maxLevel) {
        List<List<Linkage>> lists = new ArrayList<>(maxLevel);
        Stream.range(0, maxLevel).forEach(index -> lists.add(getPreviewDataSource()));
        for (int i = 0; i < maxLevel; i++) {
            Linkage linkage = lists.get(i).get(0);
            if (i < maxLevel - 1) {
                linkage.setLinkages(lists.get(i + 1));
            }
        }
        return lists.get(0);
    }

    private static List<Linkage> getPreviewDataSource() {
        return Stream.range(0, 10).map(index -> Linkage.create(String.format("item%s", index))).collect(Collectors.toList());
    }
}
