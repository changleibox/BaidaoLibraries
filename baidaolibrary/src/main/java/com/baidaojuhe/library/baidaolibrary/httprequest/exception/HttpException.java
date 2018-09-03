/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.httprequest.exception;

import android.util.SparseArray;

import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import java.io.IOException;

/**
 * Created by Box on 17/3/15.
 * <p/>
 * 错误
 */
@SuppressWarnings({"unused", "ThrowableInstanceNeverThrown", "WeakerAccess"})
public class HttpException extends IOException {

    private static final SparseArray<Error> ERROR_SPARSE_ARRAY = new SparseArray<>();
    private static final long serialVersionUID = 8645845574230995197L;

    static {
        Stream.of(Error.values()).filter(BDUtils::nonNull).forEach(error -> ERROR_SPARSE_ARRAY.put(error.code, error));
    }

    public static final HttpException ERROR_OTHER = new HttpException(Error.ERROR_OTHER);

    public static final int CODE_NOT_DATA = 201;
    public static final int CODE_TOKEN_INVALID = 401;

    private final int code;

    public HttpException(Error error) {
        this(error.message, error.code);
    }

    public HttpException(String msg, int code) {
        super(getMessage(code, msg));
        this.code = code;
    }

    public HttpException(String msg, int code, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || (obj != null && obj instanceof HttpException && ((HttpException) obj).getCode() == getCode());
    }

    public boolean equals(HttpException e) {
        return equals(this, e);
    }

    public boolean equals(int code) {
        return this.code == code;
    }

    public static boolean equals(Throwable e1, int code) {
        return e1 != null && e1 instanceof HttpException && ((HttpException) e1).equals(code);
    }

    public static boolean equals(Throwable e1, HttpException e2) {
        return !(e1 == null || e2 == null) && e2.equals(e1);
    }

    public static HttpException getException(int code) {
        Error error = ERROR_SPARSE_ARRAY.get(code);
        return error == null ? ERROR_OTHER : new HttpException(error.message, error.code);
    }

    public static boolean isNotData(Throwable e) {
        return e != null && e instanceof HttpException && ((HttpException) e).equals(CODE_NOT_DATA);
    }

    private static String getMessage(int code, String msg) {
        Error error = ERROR_SPARSE_ARRAY.get(code);
        return error == null ? msg : error.message;
    }

    protected enum Error {

        ERROR_OTHER(-1, "other error");

        private final int code;
        private final String message;

        Error(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
