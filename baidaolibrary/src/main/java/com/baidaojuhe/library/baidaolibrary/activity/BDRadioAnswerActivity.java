/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.RadioAnswerAdapter;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;

import java.util.Collections;
import java.util.List;

/**
 * Created by box on 2017/8/23.
 * <p>
 * 单选答案
 */

public abstract class BDRadioAnswerActivity extends BDBaseAnswerActivity {

    private RadioAnswerAdapter mAnswerAdapter;

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
        mAnswerAdapter = new RadioAnswerAdapter();
        mAnswerAdapter.set(presetAnswers);
        if (answers != null && !answers.isEmpty()) {
            mAnswerAdapter.setSelectedItem(answers.get(0));
        }
        return mAnswerAdapter;
    }

    @NonNull
    @Override
    protected List<NaireQuestion> getSelectedAnswers() {
        return Collections.singletonList(mAnswerAdapter.getSelectedItem());
    }
}
