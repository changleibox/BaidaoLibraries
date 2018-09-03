/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.impl;

import android.content.Intent;

/**
 * Created by box on 2017/8/18.
 * <p>
 * 自定义的上下文扩展接口
 */

public interface ContextImpl {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onActivityReenter(int resultCode, Intent data);

    void addActivityListener(ActivityListener listener);

    void removeActivityListener(ActivityListener listener);
}
