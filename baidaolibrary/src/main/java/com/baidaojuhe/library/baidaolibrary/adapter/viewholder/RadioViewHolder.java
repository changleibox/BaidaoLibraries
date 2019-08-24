/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RadioButton;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.RadioAdapter;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import net.box.app.library.adapter.IArrayAdapter;
import net.box.app.library.helper.IAppHelper;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 单选
 */

public abstract class RadioViewHolder extends BaseViewHolder {

    private RadioButton mRadioButton;
    private AppCompatTextView mTvName;
    private View mDividerBottom;

    RadioViewHolder(@NonNull ViewGroup parent) {
        super(R.layout.bd_item_radio, parent);
        mRadioButton = IViewCompat.findById(itemView, R.id.bd_radio_button);
        mTvName = IViewCompat.findById(itemView, R.id.bd_tv_name);
        mDividerBottom = IViewCompat.findById(itemView, R.id.bd_divider_bottom);
    }

    @Override
    public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
        super.onBindDatas(adapter, position);
        RadioAdapter radioAdapter = (RadioAdapter) adapter;
        mRadioButton.setChecked(radioAdapter.getSelectedPosition() == position);

        MarginLayoutParams params = (MarginLayoutParams) mDividerBottom.getLayoutParams();
        params.leftMargin = position == adapter.getItemCount() - 1 ? 0 : IAppHelper.getDimensionPixelSize(R.dimen.sizeMarginNormal);
        params.rightMargin = params.leftMargin;
        mDividerBottom.setLayoutParams(params);

        mTvName.setText(getItem(radioAdapter, position));
    }

    protected abstract String getItem(RadioAdapter adapter, int position);
}
