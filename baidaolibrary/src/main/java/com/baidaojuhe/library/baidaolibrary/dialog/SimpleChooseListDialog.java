/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.adapter.SimpleArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.impl.OnItemClickListener;

import net.box.app.library.adapter.IArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by box on 2017/8/22.
 * <p>
 * 选择楼盘
 */

@SuppressWarnings("WeakerAccess")
public class SimpleChooseListDialog extends ChooseListDialog implements OnItemClickListener {

    private SimpleArrayAdapter mArrayAdapter;
    private OnItemClickListener mItemClickListener;
    private int mSelectedPosition = -1;

    public SimpleChooseListDialog(Context context) {
        super(context);
        setAdapter(mArrayAdapter = new SimpleArrayAdapter());
        mArrayAdapter.setOnItemClickListener(this);
    }

    public String[] getStringArray(@ArrayRes int id) {
        return getContext().getResources().getStringArray(id);
    }

    public void addAll(List<String> items) {
        mArrayAdapter.set(items);
    }

    public void addAll(String... items) {
        mArrayAdapter.set(items);
    }

    public void addAll(@ArrayRes int itemsId) {
        addAll(getContext().getResources().getStringArray(itemsId));
    }

    @Nullable
    public String getItem(int position) {
        return mArrayAdapter.getItem(position);
    }

    public String getSelectedItem() {
        return getItem(mSelectedPosition);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public boolean isSelectedItem() {
        return mSelectedPosition != -1;
    }

    public void select(int position) {
        mSelectedPosition = position;
        mArrayAdapter.setSelectedPosition(position);
        if (mItemClickListener != null && isSelectedItem()) {
            mItemClickListener.onItemClick(this, getSelectedItem(), position);
        }
    }

    @Override
    public void onItemClick(IArrayAdapter<?, ?> parent, View view, int position, long id) {
        select(position);
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(SimpleChooseListDialog dialog, String item, int position);
    }

    public static List<String> getTestEstates() {
        List<String> estateNames = new ArrayList<>();
        estateNames.add("万科.幸福里");
        estateNames.add("月光湖");
        estateNames.add("天上名苑");
        estateNames.add("卓锦万代");
        estateNames.add("天府上岛");
        return Collections.unmodifiableList(estateNames);
    }
}
