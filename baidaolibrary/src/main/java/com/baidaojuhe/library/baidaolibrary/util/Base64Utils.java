/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;

@SuppressWarnings({"WeakerAccess", "CharsetObjectCanBeUsed"})
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

    public static byte[] decode(String text) {
        return Base64.decode(text, Base64.DEFAULT);
    }

    @NonNull
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        //noinspection ResultOfMethodCallIgnored
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    @NonNull
    public static Bitmap stringToBitmap(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}


