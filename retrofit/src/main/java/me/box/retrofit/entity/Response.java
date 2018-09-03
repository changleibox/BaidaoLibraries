/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.entity;

import me.box.retrofit.exception.HttpException;

/**
 * Created by box on 2017/5/3.
 * <p>
 * http 返回数据
 */

public class Response<T> extends BaseResponse {

    /**
     * code : string
     * data : {}
     * msg : string
     * success : true
     */

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setException(HttpException e) {
        setCode(e.getCode());
        setMsg(e.getMessage());
    }

}
