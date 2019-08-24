/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.ArrayRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.adapter.BottomOpsAdapter;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import java.util.Collection;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 底部有操作按钮的对话框
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class BottomOpsDialog extends BDBottomSheetDialog {

    private BottomOpsAdapter mOperationAdapter;

    public BottomOpsDialog(Context context) {
        super(context);
        setContentView(R.layout.bd_dialog_bottom_ops);
        RecyclerView rvContent = IViewCompat.findById(this, R.id.bd_rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(context));
        rvContent.setAdapter(mOperationAdapter = new BottomOpsAdapter());

        IViewCompat.findById(this, R.id.bd_btn_cancel).setOnClickListener(this::onCancel);
    }

    public void addAll(@ArrayRes int id) {
        addAll(getContext().getResources().getStringArray(id));
    }

    public void addAll(String... items) {
        mOperationAdapter.set(items);
    }

    public void addAll(Collection<String> collection) {
        mOperationAdapter.set(collection);
    }

    public String getItem(int position) {
        return mOperationAdapter.getItem(position);
    }

    public boolean isEmpty() {
        return mOperationAdapter.getItemCount() == 0;
    }

    public void onCancel(View v) {
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOperationAdapter.setOnItemClickListener((parent, view, position, id) -> {
            dismiss();
            if (listener != null) {
                listener.onItemClick(BottomOpsDialog.this, view, position);
            }
        });
    }

    public interface OnItemClickListener {

        void onItemClick(DialogInterface dialog, View view, int position);

    }
}
