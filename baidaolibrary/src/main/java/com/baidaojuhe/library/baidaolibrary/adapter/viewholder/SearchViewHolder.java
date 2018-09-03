/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public class SearchViewHolder extends BaseViewHolder {

    private boolean isHistoryRecord;

    public SearchViewHolder(@LayoutRes int id, ViewGroup parent) {
        super(id, parent);
    }

    public SearchViewHolder(View itemView) {
        super(itemView);
    }

    public SearchViewHolder(Context context, View itemView) {
        super(context, itemView);
    }

    public SearchViewHolder(@NonNull Context context, @NonNull View itemView, boolean bindViews) {
        super(context, itemView, bindViews);
    }

    public final int getRealPosition() {
        int adapterPosition = getAdapterPosition();
        return isHistoryRecord ? adapterPosition - 1 : adapterPosition;
    }

    public final SearchViewHolder setHistoryRecord(boolean historyRecord) {
        isHistoryRecord = historyRecord;
        return this;
    }
}