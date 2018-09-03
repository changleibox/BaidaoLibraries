/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.os.Bundle;

import com.baidaojuhe.library.baidaolibrary.compat.BundleCompat;

/**
 * Created by box on 2017/9/19.
 * <p>
 * 模态activity
 */

public abstract class BDModalityActivity extends BDActionBarActivity {

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        Bundle bundle = getBundle();
        bundle.putAll(BundleCompat.BUNDLE_MODALITY);
        getIntent().putExtras(bundle);
        return super.onSetContentViewBefore(savedInstanceState);
    }

}
