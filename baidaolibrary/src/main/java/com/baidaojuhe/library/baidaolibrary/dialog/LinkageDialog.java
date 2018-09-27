/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.entity.Linkage;
import com.baidaojuhe.library.baidaolibrary.widget.LinkageView;

import java.util.List;

/**
 * Created by box on 2017/4/18.
 * <p>
 * n级联动
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class LinkageDialog extends ChooseDialog {

    private final LinkageView mLinkageView;

    public LinkageDialog(Context context) {
        this(context, null);
    }

    LinkageDialog(Context context, OnSelectedListener listener) {
        super(context);
        setContentView(R.layout.bd_dialog_linkage);
        mLinkageView = findViewById(R.id.bd_linkage);
        if (mLinkageView != null) {
            mLinkageView.setOnSelectedListener(getSelectedListener(listener));
        }

        setLevel(LinkageView.LEVEL_3);
    }

    public void setLevel(@LinkageView.Level int maxLevel) {
        mLinkageView.setMaxLevel(maxLevel);

        List<Linkage> linkages = getLinkages();
        if (linkages != null) {
            setLinkages(linkages);
        }
    }

    @Override
    public void dismiss() {
        if (!isShowing()) {
            return;
        }
        super.dismiss();
    }

    public void setOnSelectedListener(OnSelectedListener l) {
        if (mLinkageView != null) {
            mLinkageView.setOnSelectedListener(getSelectedListener(l));
        }
    }

    public void performSelected() {
        performSelected(getSelectedPositions());
    }

    public void performSelected(Integer[] selectedPositions) {
        mLinkageView.performSelected(selectedPositions);
    }

    public void setItemSelected(Integer[] selectedPositions) {
        mLinkageView.setItemSelected(selectedPositions);
    }

    public void setLinkages(@NonNull List<Linkage> linkages) {
        mLinkageView.setLinkages(linkages);
    }

    public Linkage[] getLinkages(@NonNull Integer[] positions) {
        return mLinkageView.getLinkages(positions);
    }

    public Integer[] getSelectedPositions() {
        return mLinkageView.getSelectedPositions();
    }

    @Nullable
    public abstract Linkage getDefaultLinkage();

    @Nullable
    public abstract List<Linkage> getLinkages();

    private LinkageView.OnSelectedListener getSelectedListener(OnSelectedListener listener) {
        if (listener == null) {
            return null;
        }
        return new LinkageView.OnSelectedListener() {
            @Override
            public void onSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out) {
                listener.onSelected(LinkageDialog.this, positions, out);
            }

            @Override
            public void onLastSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out) {
                listener.onLastSelected(LinkageDialog.this, positions, out);

            }
        };
    }

    public interface OnSelectedListener {
        void onSelected(LinkageDialog dialog, Integer[] positions, @Nullable Linkage out);

        void onLastSelected(LinkageDialog dialog, Integer[] positions, @Nullable Linkage out);
    }

}
