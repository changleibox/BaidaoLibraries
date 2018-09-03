/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by box on 2017/8/17.
 * <p>
 * 各种格式化
 */

public class FormatCompat {

    private static final DecimalFormat FORMAT_FLOAT = new DecimalFormat("###,##0.00");

    public static String format(double currency) {
        return DecimalFormat.getInstance(Locale.CHINA).format(currency);
    }

    public static String formatFloat(double currency) {
        return FORMAT_FLOAT.format(currency);
    }

    public static String formatInteger(double currency) {
        return DecimalFormat.getIntegerInstance(Locale.CHINA).format(currency);
    }

    public static String formatCurrency(double currency) {
        return DecimalFormat.getCurrencyInstance(Locale.CHINA).format(currency / 100);
    }

    public static String formatCurrencyYuan(double currency) {
        return DecimalFormat.getCurrencyInstance(Locale.CHINA).format(currency);
    }

    public static String formatPercent(double currency) {
        return DecimalFormat.getPercentInstance(Locale.CHINA).format(currency);
    }

    public static String formatFloatPercent(double currency) {
        return new DecimalFormat("###,##0.00%").format(currency);
    }

    public static String formatAcreage(double acreage) {
        return String.format(Locale.CHINA, "%sm²", format(acreage));
    }

    public static Number parsePercent(String source) {
        try {
            return DecimalFormat.getPercentInstance(Locale.CHINA).parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
