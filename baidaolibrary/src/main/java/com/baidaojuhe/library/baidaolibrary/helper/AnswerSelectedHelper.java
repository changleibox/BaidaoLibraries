/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.helper;

import com.baidaojuhe.library.baidaolibrary.entity.Answer;
import com.baidaojuhe.library.baidaolibrary.impl.OnAnswerSelectedImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by box on 2018/5/2.
 * <p>
 * 用于选中到访问卷答案
 */
public class AnswerSelectedHelper implements OnAnswerSelectedImpl {

    private final List<Answer> mSelectedAnswers = new ArrayList<>();
    private final boolean isSingleSelection;

    public AnswerSelectedHelper(boolean isSingleSelection) {
        this.isSingleSelection = isSingleSelection;
    }

    @Override
    public boolean isSelected(Answer answer) {
        return mSelectedAnswers.contains(answer);
    }

    @Override
    public void setSelected(Answer answer, boolean isChecked) {
        if (isSingleSelection) {
            setSelectedItems(Collections.singletonList(answer));
            return;
        }
        mSelectedAnswers.remove(answer);
        if (isChecked) {
            mSelectedAnswers.add(answer);
        }
    }

    @Override
    public List<Answer> getSelectedItems() {
        return Collections.unmodifiableList(mSelectedAnswers);
    }

    @Override
    public void setSelectedItems(List<Answer> items) {
        mSelectedAnswers.clear();
        if (items != null) {
            mSelectedAnswers.addAll(items);
        }
    }
}
