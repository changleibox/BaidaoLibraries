/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.entity;

import me.box.retrofit.exception.HttpException;

/**
 * Created by box on 2017/5/3.
 * <p>
 * 返回的基本数据
 */

public class BaseResponse {

    private static final int SUCCESS_CODE = 200;

    private int code;
    private String message;

    public int getCode() {
        int code = HttpException.ERROR_OTHER.getCode();
        try {
            code = this.code;
        } catch (Exception ignored) {
        }
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public boolean isSuccess() {
        return code == SUCCESS_CODE;
    }

}
