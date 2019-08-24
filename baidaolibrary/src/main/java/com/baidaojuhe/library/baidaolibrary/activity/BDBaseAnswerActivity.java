/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.compat.BundleCompat;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 到访问卷答案
 */

public abstract class BDBaseAnswerActivity extends BDBaseAppCompatActivity {

    private ArrayAdapter<?, ?> mArrayAdapter;

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        List<NaireQuestion> presetAnswers = BundleCompat.getParcelables(this, BDKey.KEY_PRESET_ANSWERS);
        List<NaireQuestion> answers = BundleCompat.getParcelables(this, BDKey.KEY_SELECTED_ANSWERS);
        mArrayAdapter = getAdapter(presetAnswers, answers);
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecyclerView rvAnswer = IViewCompat.findById(this, R.id.bd_rv_answer);
        setTitle(getBundle().getCharSequence(BDKey.KEY_TITLE));
        rvAnswer.setAdapter(mArrayAdapter);

        findViewById(R.id.bd_btn_confirm).setOnClickListener(this::onConfirmClicked);
    }

    @Override
    public final BDLayout getContainerLayout(@NonNull Bundle bundle, Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_naire_answer);
    }

    protected void onConfirmClicked(View v) {
        final ArrayList<NaireQuestion> selectedAnswers = new ArrayList<>(Stream.of(getSelectedAnswers()).filter(BDUtils::nonNull).collect(Collectors.toList()));
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(BDKey.KEY_SELECTED_ANSWERS, selectedAnswers);
        setResult(RESULT_OK, intent);
        finish();
    }

    @NonNull
    protected abstract ArrayAdapter<?, ?> getAdapter(List<NaireQuestion> presetAnswers, List<NaireQuestion> answers);

    @NonNull
    protected abstract List<NaireQuestion> getSelectedAnswers();
}
