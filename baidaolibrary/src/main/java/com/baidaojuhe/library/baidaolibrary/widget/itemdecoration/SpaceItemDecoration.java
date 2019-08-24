/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import net.box.app.library.util.IAppUtils;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private boolean hasBorder;

    public SpaceItemDecoration(int interval) {
        this(interval, interval, false);
    }

    public SpaceItemDecoration(int horizontalSpacing, int verticalSpacing, boolean hasBorder) {
        this.mHorizontalSpacing = horizontalSpacing;
        this.mVerticalSpacing = verticalSpacing;
        this.hasBorder = hasBorder;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int itemCount = parent.getLayoutManager().getItemCount();
        int spanCount = getSpanCount(parent);
        int rowCount = itemCount / spanCount + (itemCount % spanCount > 0 ? 1 : 0);
        float[] topBottomMargin = IAppUtils.getLeftRightOrTopBottomMargin(rowCount, position / spanCount, hasBorder);
        float[] leftRightMargin = IAppUtils.getLeftRightOrTopBottomMargin(spanCount, position % spanCount, hasBorder);
        outRect.left = (int) (leftRightMargin[0] * mHorizontalSpacing);
        outRect.right = (int) (leftRightMargin[1] * mHorizontalSpacing);
        outRect.top = (int) (topBottomMargin[0] * mVerticalSpacing);
        outRect.bottom = (int) (topBottomMargin[1] * mVerticalSpacing);
    }

    //返回列数
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

}