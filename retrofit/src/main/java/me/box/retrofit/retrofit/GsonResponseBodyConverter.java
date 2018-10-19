/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        final String json = value.string();
        if (TextUtils.isEmpty(json)) {
            throw HttpException.ERROR_NOT_DATA;
        }
        if (!isBaseResponse(json)) {
            return adapter.fromJson(json);
        }
        final BaseResponse baseResponse = gson.fromJson(json, BaseResponse.class);
        if (!baseResponse.isSuccess()) {
            throw new HttpException(baseResponse.getMsg(), baseResponse.getCode());
        }
        if (TypeCompat.isVoid(type) || TypeCompat.isNull(type)) {
            return null;
        }
        if (TypeCompat.isString(type)) {
            final T data = TypeCompat.getString(json, DATA);
            if (data == null) {
                throw HttpException.ERROR_NOT_DATA;
            }
            return data;
        }
        final Type tmpType = TypeCompat.getResponseType(type);
        if (tmpType != null) {
            Response<T> response = gson.fromJson(json, tmpType);
            final T data = response.getData();
            if (data == null) {
                throw HttpException.ERROR_NOT_DATA;
            }
            return data;
        }
        final String dataJson = TypeCompat.getString(json, DATA);
        final T data = TextUtils.isEmpty(dataJson) ? null : adapter.fromJson(dataJson);
        int length = 1;
        try {
            if (data == null || new JSONArray(dataJson).length() == 0) {
                length = 0;
            }
        } catch (JSONException ignored) {
        }
        if (length == 0) {
            throw HttpException.ERROR_NOT_DATA;
        }
        return data;
    }

    private boolean isBaseResponse(String json) {
        try {
            final JSONObject jsonObject = new JSONObject(json);
            final Class<BaseResponse> responseClass = BaseResponse.class;
            final Field[] fields = responseClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers()) && !jsonObject.has(field.getName())) {
                    return false;
                }
            }
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

}
