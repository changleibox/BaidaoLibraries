/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.support.design.widget.TabLayout;

import com.baidaojuhe.library.baidaolibrary.listener.OnTabSelectedListener;

import net.box.app.library.IFragment;
import net.box.app.library.helper.IFragmentHelper;

/**
 * Created by box on 2017/7/26.
 * <p>
 * Tab切换的Activity
 */

public abstract class BDBaseTabActivity extends BDBaseActivity implements OnTabSelectedListener {

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        IFragmentHelper fragmentHelper = getFragmentHelper();
        IFragment fragment = fragmentHelper.showFragment(getContainerId(), position);
        if (fragment != null && fragment.isAdded() && fragment instanceof OnTabSelectedListener) {
            ((OnTabSelectedListener) fragment).onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        IFragment fragment = getFragmentHelper().getFragment(position);
        if (fragment != null && fragment.isAdded() && fragment instanceof OnTabSelectedListener) {
            ((OnTabSelectedListener) fragment).onTabUnselected(tab);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        IFragment fragment = getFragmentHelper().getFragment(position);
        if (fragment != null && fragment.isAdded() && fragment instanceof OnTabSelectedListener) {
            ((OnTabSelectedListener) fragment).onTabReselected(tab);
        }
    }
}
