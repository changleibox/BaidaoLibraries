/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.activity.BDWebActivity;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;

import net.box.app.library.helper.IAppHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Box on 16/7/15.
 * <p/>
 * 打开超链接
 */
@SuppressWarnings("WeakerAccess")
public class OpenWebUtils {

    //利用正则识别
    @SuppressWarnings("unused")
    public static CharSequence checkAutoLink(Context context, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        Pattern pattern = Pattern.compile(IAppHelper.getString(R.string.bd_lable_url_reg_2));
        Matcher matcher = pattern.matcher(spannableStringBuilder);
        while (matcher.find()) {
            setClickableSpan(context, spannableStringBuilder, matcher);
        }

        Pattern pattern2 = Pattern.compile(IAppHelper.getString(R.string.bd_lable_url_reg));
        matcher.reset();
        matcher = pattern2.matcher(spannableStringBuilder);
        while (matcher.find()) {
            setClickableSpan(context, spannableStringBuilder, matcher);
        }
        return spannableStringBuilder;
    }

    //给符合的设置自定义点击事件
    public static void setClickableSpan(final Context context, final SpannableStringBuilder clickableHtmlBuilder, final Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        final String url = matcher.group();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openWeb(context, null, url);
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    public static void openWeb(Context context, @Nullable String title, @NonNull String url) {
        Bundle bundle = new Bundle();
        bundle.putString(BDConstants.BDKey.KEY_TITLE, title);
        bundle.putString(BDConstants.BDKey.KEY_CONTENT, url);
        Intent intent = new Intent(context, BDWebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
