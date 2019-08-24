/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.adapter.RadioAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.RadioAnswerAdapter;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 单选答案
 */

public class RadioAnswerViewHolder extends RadioViewHolder {

    public RadioAnswerViewHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    @Override
    protected String getItem(RadioAdapter adapter, int position) {
        String[] names = ((RadioAnswerAdapter) adapter).getItem(position).getQuestionName().split("-");
        return names.length > 1 ? names[1] : names[0];
    }
}
