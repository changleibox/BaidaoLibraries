/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary;

import android.os.Build;
import android.os.StrictMode;

import net.box.app.library.IApplication;

/**
 * Created by box on 2017/11/22.
 * <p>
 * 应用程序启动入口
 */

public class BDApplication extends IApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }
}
