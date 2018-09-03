/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.util.Base64;

@SuppressWarnings("WeakerAccess")
public class Base64Utils {

    private static final String UTF_8 = "UTF-8";

    public static String encodeToString(String text) throws Exception {
        return Base64.encodeToString(text.getBytes(UTF_8), Base64.DEFAULT);
    }

    public static byte[] encode(String text) throws Exception {
        return Base64.encode(text.getBytes(UTF_8), Base64.DEFAULT);
    }

    public static String decodeToString(String text) throws Exception {
        return new String(decode(text), UTF_8);
    }

    public static byte[] decode(String text) throws Exception {
        return Base64.decode(text, Base64.DEFAULT);
    }

}


