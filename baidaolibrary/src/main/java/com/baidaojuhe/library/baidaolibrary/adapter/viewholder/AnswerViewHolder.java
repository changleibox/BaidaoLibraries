/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.AnswerAdapter;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.entity.Answer;

import net.box.app.library.adapter.IArrayAdapter;

/**
 * Created by box on 2018/5/2.
 * <p>
 * 到访问卷答案选择
 */
public class AnswerViewHolder extends BaseViewHolder {

    private CompoundButton mRadioButton;
    private AppCompatTextView mTvName;

    public AnswerViewHolder(@NonNull ViewGroup parent, boolean isSingleSelection) {
        super(isSingleSelection ? R.layout.bd_item_radio : R.layout.bd_item_checkbox, parent);
        mRadioButton = IViewCompat.findById(itemView, R.id.bd_radio_button);
        mTvName = IViewCompat.findById(itemView, R.id.bd_tv_name);
    }

    @Override
    public void onBindDatas(@NonNull IArrayAdapter adapter, int position) {
        super.onBindDatas(adapter, position);
        Answer answer = (Answer) adapter.getItem(position);
        mTvName.setText(answer.getContent());

        if (!(adapter instanceof AnswerAdapter)) {
            return;
        }
        AnswerAdapter answerAdapter = (AnswerAdapter) adapter;
        mRadioButton.setChecked(answerAdapter.isSelected(answer));
        itemView.setOnClickListener(view -> answerAdapter.setSelected(answer));
    }
}
