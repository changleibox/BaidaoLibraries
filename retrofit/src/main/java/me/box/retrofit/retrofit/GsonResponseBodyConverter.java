/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

import me.box.retrofit.compat.TypeCompat;
import me.box.retrofit.entity.BaseResponse;
import me.box.retrofit.entity.Response;
import me.box.retrofit.exception.HttpException;
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
        if (TypeCompat.isString(type)) {
            return TypeCompat.getString(json, DATA);
        }
        Type tmpType = TypeCompat.getResponseType(type);
        if (tmpType != null) {
            Response<T> response = gson.fromJson(json, tmpType);
            return response.getData();
        }
        String data = TypeCompat.getString(json, DATA);
        return TextUtils.isEmpty(data) ? null : adapter.fromJson(data);
    }

}
