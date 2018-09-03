/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.retrofit;

import android.text.TextUtils;

import com.baidaojuhe.library.baidaolibrary.compat.TypeCompat;
import com.baidaojuhe.library.baidaolibrary.httprequest.entity.BaseResponse;
import com.baidaojuhe.library.baidaolibrary.httprequest.entity.Response;
import com.baidaojuhe.library.baidaolibrary.httprequest.exception.HttpException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import net.box.app.library.util.IJsonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final String DATA = "data";

    private final Type type;
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Type type, Gson gson, TypeAdapter<T> adapter) {
        this.type = type;
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        BaseResponse baseResponse = gson.fromJson(json, BaseResponse.class);
        if (!baseResponse.isSuccess()) {
            throw new HttpException(baseResponse.getMsg(), baseResponse.getCode());
        }
        if (TypeCompat.isVoid(type)) {
            return null;
        }
        Type tmpType = TypeCompat.getResponseType(type);
        if (tmpType != null) {
            Response<T> response = gson.fromJson(json, tmpType);
            return response.getData();
        }
        String data = IJsonDecoder.getString(json, DATA);
        return TextUtils.isEmpty(data) ? null : adapter.fromJson(data);
    }

}
