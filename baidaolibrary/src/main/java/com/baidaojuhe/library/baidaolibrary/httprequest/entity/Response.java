/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.entity;

import com.baidaojuhe.library.baidaolibrary.httprequest.exception.HttpException;

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
