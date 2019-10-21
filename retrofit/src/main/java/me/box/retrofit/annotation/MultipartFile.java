/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.retrofit.annotation;

import androidx.annotation.NonNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by box on 2019-10-21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MultipartFile {

    @NonNull
    Type value();

    enum Type {
        PATH, BASE64
    }
}
