/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.listener;

import androidx.annotation.IdRes;

import com.google.android.material.tabs.TabLayout;

/**
 * Created by box on 2017/10/23.
 * <p>
 * Callback interface invoked when a tab's selection state changes.
 */

public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener {

    @IdRes
    int getContainerId();

}
