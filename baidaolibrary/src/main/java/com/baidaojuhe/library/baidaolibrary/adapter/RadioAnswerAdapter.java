/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.RadioAnswerViewHolder;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 单选答案
 */

public class RadioAnswerAdapter extends RadioAdapter<NaireQuestion, RadioAnswerViewHolder> {

    @NonNull
    @Override
    public RadioAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RadioAnswerViewHolder(parent);
    }

}
