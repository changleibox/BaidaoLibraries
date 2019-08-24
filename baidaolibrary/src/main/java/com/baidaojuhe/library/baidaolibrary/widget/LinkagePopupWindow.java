/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.entity.Linkage;

import java.util.List;

/**
 * Created by box on 2017/11/28.
 * <p>
 * n级联动
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class LinkagePopupWindow extends HasBackgroundPopupWindow {

    private final LinkageView mLinkageView;

    public LinkagePopupWindow(Context context) {
        this(context, null);
    }

    LinkagePopupWindow(Context context, LinkagePopupWindow.OnSelectedListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.bd_widget_popup_window_linkage, null),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
        setTouchable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mLinkageView = getContentView().findViewById(R.id.bd_linkage);
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

    public void setOnSelectedListener(LinkagePopupWindow.OnSelectedListener l) {
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

    private LinkageView.OnSelectedListener getSelectedListener(LinkagePopupWindow.OnSelectedListener listener) {
        if (listener == null) {
            return null;
        }
        return new LinkageView.OnSelectedListener() {
            @Override
            public void onSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out) {
                listener.onSelected(LinkagePopupWindow.this, positions, out);
            }

            @Override
            public void onLastSelected(LinkageView linkageView, Integer[] positions, @Nullable Linkage out) {
                listener.onLastSelected(LinkagePopupWindow.this, positions, out);
            }
        };
    }

    public interface OnSelectedListener {
        void onSelected(LinkagePopupWindow dialog, Integer[] positions, @Nullable Linkage out);

        void onLastSelected(LinkagePopupWindow dialog, Integer[] positions, @Nullable Linkage out);
    }

    public class OnSimpleSelectedListener implements OnSelectedListener {
        @Override
        public void onSelected(LinkagePopupWindow dialog, Integer[] positions, @Nullable Linkage out) {
        }

        @Override
        public void onLastSelected(LinkagePopupWindow dialog, Integer[] positions, @Nullable Linkage out) {
        }
    }
}
