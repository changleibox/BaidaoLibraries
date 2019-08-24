/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.ArrayAdapter;
import com.baidaojuhe.library.baidaolibrary.adapter.viewholder.BaseViewHolder;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 底部的选择列表对话框基类
 */

@SuppressWarnings("WeakerAccess")
public class ChooseListDialog extends ChooseDialog {

    private final RecyclerView mRvContent;

    public ChooseListDialog(Context context) {
        super(context);
        setContentView(R.layout.bd_dialog_choose_list);
        mRvContent = IViewCompat.findById(this, R.id.bd_rv_content);

        mRvContent.setLayoutManager(new LinearLayoutManager(context));
    }

    public <T, VH extends BaseViewHolder> void setAdapter(ArrayAdapter<T, VH> adapter) {
        mRvContent.setAdapter(adapter);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRvContent.setAdapter(adapter);
    }

    public int getItemCount() {
        return mRvContent.getChildCount();
    }
}
