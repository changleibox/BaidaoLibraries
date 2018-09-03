/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

/**
 * Created by box on 2017/11/1.
 * <p>
 * 可收缩的
 */

public interface Expandable {

    void onContentChanged();

    boolean isExpanded(int position);

    void setExpanded(int position);

    void setCollapsed(int position);

    void switchExpandedStatus(int position);
}
