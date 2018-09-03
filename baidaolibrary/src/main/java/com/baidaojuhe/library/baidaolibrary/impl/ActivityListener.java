/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import android.content.Intent;

public interface ActivityListener {
    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onActivityReenter(int resultCode, Intent data);

    abstract class OnActivityResultListener implements ActivityListener {
        @Override
        public void onActivityReenter(int resultCode, Intent data) {
        }
    }

    abstract class OnActivityReenterListener implements ActivityListener {
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        }
    }
}