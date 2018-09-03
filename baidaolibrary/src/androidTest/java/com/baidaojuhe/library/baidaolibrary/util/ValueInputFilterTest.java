/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.text.SpannedString;

import junit.framework.Assert;

/**
 * Created by box on 2017/12/29.
 * <p>
 * 限制ediitext输入数字的最大值和最小值
 */
public class ValueInputFilterTest {

    private static final String VALUE = "0000";

    private final ValueInputFilter inputFilter = new ValueInputFilter(1, 10);

    @org.junit.Test
    public void inputFilter() {
        CharSequence filter = inputFilter.filter(null, 0, 0, new SpannedString(VALUE), 0, VALUE.length());
        if (filter == null) {
            filter = VALUE;
        }

        Assert.assertEquals(Double.valueOf(filter.toString()), 0d); // 验证result==3，如果不正确，测试不通过
    }

}