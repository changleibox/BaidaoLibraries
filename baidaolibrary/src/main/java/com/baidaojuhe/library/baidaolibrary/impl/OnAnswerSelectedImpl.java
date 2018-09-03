/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import com.baidaojuhe.library.baidaolibrary.entity.Answer;

import java.util.List;

public interface OnAnswerSelectedImpl {

    boolean isSelected(Answer answer);

    void setSelected(Answer answer, boolean isChecked);

    List<Answer> getSelectedItems();

    void setSelectedItems(List<Answer> items);
}