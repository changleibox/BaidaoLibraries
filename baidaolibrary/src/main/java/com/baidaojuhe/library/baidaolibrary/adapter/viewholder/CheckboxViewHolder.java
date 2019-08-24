/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.CheckboxAdapter;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.helper.IAppHelper;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 多选
 */

public abstract class CheckboxViewHolder extends BaseViewHolder {

    private AppCompatCheckBox mRadioButton;
    private AppCompatTextView mTvName;
    private View mDividerBottom;

    CheckboxViewHolder(@NonNull ViewGroup parent) {
        super(R.layout.bd_item_checkbox, parent);
        mRadioButton = IViewCompat.findById(itemView, R.id.bd_radio_button);
        mTvName = IViewCompat.findById(itemView, R.id.bd_tv_name);
        mDividerBottom = IViewCompat.findById(itemView, R.id.bd_divider_bottom);
    }

    @Override
    public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
        super.onBindDatas(adapter, position);
        CheckboxAdapter checkboxAdapter = (CheckboxAdapter) adapter;
        mRadioButton.setChecked(checkboxAdapter.isSelected(position));

        MarginLayoutParams params = (MarginLayoutParams) mDividerBottom.getLayoutParams();
        params.leftMargin = position == adapter.getItemCount() - 1 ? 0 : IAppHelper.getDimensionPixelSize(R.dimen.sizeMarginNormal);
        params.rightMargin = params.leftMargin;
        mDividerBottom.setLayoutParams(params);

        mTvName.setText(getItem(checkboxAdapter, position));
    }

    protected abstract String getItem(CheckboxAdapter adapter, int position);
}
