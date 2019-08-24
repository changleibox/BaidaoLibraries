/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.adapter.CheckboxAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.CheckboxAnswerAdapter;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 多选答案
 */

public class CheckboxAnswerViewHolder extends CheckboxViewHolder {

    public CheckboxAnswerViewHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    @Override
    protected String getItem(CheckboxAdapter adapter, int position) {
        String[] names = ((CheckboxAnswerAdapter) adapter).getItem(position).getQuestionName().split("-");
        return names.length > 1 ? names[1] : names[0];
    }
}
