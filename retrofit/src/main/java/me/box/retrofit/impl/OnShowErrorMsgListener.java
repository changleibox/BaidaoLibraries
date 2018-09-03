/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.impl;

/**
 * Created by box on 2018/5/28.
 * <p>
 * 显示网络请求错误信息
 */
public interface OnShowErrorMsgListener {

    void onShowErrorMsg(Throwable throwable);
}
