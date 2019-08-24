/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.CheckboxAnswerViewHolder;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 多选答案
 */

public class CheckboxAnswerAdapter extends CheckboxAdapter<NaireQuestion, CheckboxAnswerViewHolder> {

    @NonNull
    @Override
    public CheckboxAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckboxAnswerViewHolder(parent);
    }

}
