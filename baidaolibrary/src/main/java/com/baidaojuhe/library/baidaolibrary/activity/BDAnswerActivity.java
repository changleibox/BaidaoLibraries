/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.AnswerAdapter;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.compat.BundleCompat;
import com.baidaojuhe.library.baidaolibrary.entity.Answer;
import com.baidaojuhe.library.baidaolibrary.entity.NaireQuestion;
import com.baidaojuhe.library.baidaolibrary.helper.AnswerSelectedHelper;
import com.baidaojuhe.library.baidaolibrary.impl.IContext;
import com.baidaojuhe.library.baidaolibrary.impl.OnAnswerSelectedImpl;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * Created by box on 2018/5/2.
 * <p>
 * 到访问卷答案选择
 */
public abstract class BDAnswerActivity extends BDBaseActivity implements Observer<List<Answer>>, OnAnswerSelectedImpl {

    private RecyclerView mRvAnswer;

    private AnswerAdapter mAnswerAdapter;
    private AnswerSelectedHelper mSelectedHelper;
    private List<NaireQuestion> mSelectedAnswers;

    private final List<Answer> mAllAnswers = new ArrayList<>();

    @Override
    public void onInitViews(@NonNull Bundle bundle, Bundle savedInstanceState) {
        boolean isSingleSelection = getBundle().getBoolean(BDKey.KEY_SINGLE_SELECTION);
        mSelectedHelper = new AnswerSelectedHelper(isSingleSelection);
        mAnswerAdapter = new AnswerAdapter(this, isSingleSelection);
        mRvAnswer = (RecyclerView) findViewById(R.id.bd_rv_answer);
    }

    @Override
    public void onInitDatas(@NonNull Bundle bundle, Bundle savedInstanceState) {
        CharSequence contentType = getBundle().getCharSequence(BDKey.KEY_CONTENT_TYPE);
        setTitle(contentType);
        mRvAnswer.setAdapter(mAnswerAdapter);
        List<NaireQuestion> presetAnswers = BundleCompat.getParcelables(this, BDKey.KEY_PRESET_ANSWERS);
        mSelectedAnswers = BundleCompat.getParcelables(this, BDKey.KEY_SELECTED_ANSWERS);
        if (presetAnswers != null && !presetAnswers.isEmpty()) {
            onNext(handleLocalDatas(presetAnswers));
        } else {
            getQuestionnaire(this, contentType, this);
        }
    }

    @Override
    public void onInitListeners(@NonNull Bundle bundle, Bundle savedInstanceState) {
        findViewById(R.id.bd_btn_confirm).setOnClickListener(this::onConfirmClicked);
    }

    @Override
    public BDLayout getContainerLayout(@NonNull Bundle bundle, Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_answer);
    }

    protected ArrayList<NaireQuestion> getSelectedAnswers() {
        return new ArrayList<>(Stream.of(mSelectedHelper
                .getSelectedItems()).filter(BDUtils::nonNull)
                .map(answer -> new NaireQuestion(answer.getTmpContent())).collect(Collectors.toList()));
    }

    protected void onConfirmClicked(View v) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(BDKey.KEY_SELECTED_ANSWERS, getSelectedAnswers());
        setResult(RESULT_OK, intent);
        finish();
    }

    protected abstract void getQuestionnaire(IContext context, CharSequence contentType, Observer<List<Answer>> observer);

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(List<Answer> answers) {
        mAllAnswers.clear();
        for (Answer answer : answers) {
            setAnswerTmpContent(answer);
        }
        mAnswerAdapter.set(answers);
        if (mSelectedAnswers != null) {
            mAnswerAdapter.setSelectedItems(Stream.of(mSelectedAnswers)
                    .map(Answer::new)
                    .filter(mAllAnswers::contains)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public boolean isSelected(Answer answer) {
        return mSelectedHelper.isSelected(answer);
    }

    @Override
    public void setSelected(Answer answer, boolean isChecked) {
        mSelectedHelper.setSelected(answer, isChecked);
        mAnswerAdapter.notifyDataSetChanged();
    }

    @Override
    public List<Answer> getSelectedItems() {
        return mSelectedHelper.getSelectedItems();
    }

    @Override
    public void setSelectedItems(List<Answer> items) {
        mSelectedHelper.setSelectedItems(items);
        mAnswerAdapter.notifyDataSetChanged();
    }

    private void setAnswerTmpContent(Answer answer) {
        if (!mAllAnswers.contains(answer)) {
            mAllAnswers.add(answer);
        }
        List<Answer> childs = answer.getChilds();
        if (childs == null || childs.isEmpty()) {
            return;
        }
        String parentContent = answer.getTmpContent();
        for (Answer child : childs) {
            child.setTmpContent(parentContent + getDelimiter() + child.getContent());
            if (!mAllAnswers.contains(child)) {
                mAllAnswers.add(child);
            }
            setAnswerTmpContent(child);
        }
    }

    private List<Answer> handleLocalDatas(List<NaireQuestion> presetAnswers) {
        List<Answer> tmpQuestions = new ArrayList<>();
        Map<String, List<NaireQuestion>> answerMap = new LinkedHashMap<>();
        Stream.of(presetAnswers).forEach(question -> {
            String questionName = question.getQuestionName();
            String[] names = questionName.split(getDelimiter());
            if (names.length == 1) {
                tmpQuestions.add(new Answer(questionName));
            } else {
                String superName = names[0];
                List<NaireQuestion> questions = answerMap.get(superName);
                if (questions == null) {
                    questions = new ArrayList<>();
                }
                question.setQuestionName(names[1]);
                questions.add(question);
                answerMap.put(superName, questions);
            }
        });
        Stream.of(answerMap).forEach(entity -> {
            String key = entity.getKey();
            List<NaireQuestion> questions = entity.getValue();
            Answer question = new Answer();
            question.setContent(key);
            question.setChilds(questions == null ? null : Stream
                    .of(questions).map(Answer::new).collect(Collectors.toList()));
            tmpQuestions.add(question);
        });
        return tmpQuestions;
    }

    private String getDelimiter() {
        return "-";
    }
}
