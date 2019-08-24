/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.CheckboxAnswerAdapter;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;

import java.util.List;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 多选答案
 */

public abstract class BDCheckboxAnswerActivity extends BDBaseAnswerActivity {

    private CheckboxAnswerAdapter mAnswerAdapter;

    @Override
    public void onInitViews(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onInitDatas(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onInitListeners(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    protected ArrayAdapter<?, ?> getAdapter(List<NaireQuestion> presetAnswers, List<NaireQuestion> answers) {
        mAnswerAdapter = new CheckboxAnswerAdapter();
        mAnswerAdapter.set(presetAnswers);
        mAnswerAdapter.setSelectedItems(answers);
        return mAnswerAdapter;
    }

    @NonNull
    @Override
    protected List<NaireQuestion> getSelectedAnswers() {
        return mAnswerAdapter.getSelectedItems();
    }
}
