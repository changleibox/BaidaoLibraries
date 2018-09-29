/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by box on 2017/10/19.
 * <p>
 * 可收缩的
 */

public abstract class ExpandableViewHolder extends SearchExpandableViewHolder {

    public ExpandableViewHolder(@NonNull ViewGroup parent) {
        super(parent);
    }

    public ExpandableViewHolder(int id, @NonNull ViewGroup parent) {
        super(id, parent);
    }
}
