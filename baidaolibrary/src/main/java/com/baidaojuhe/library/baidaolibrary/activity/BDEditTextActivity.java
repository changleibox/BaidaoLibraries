/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.util.BDUtils;

import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IURIUtils;

/**
 * Created by box on 2017/3/31.
 * <p>
 * 编辑文本页面
 */

public class BDEditTextActivity extends BDActionBarActivity {

    private TextView mTvName;
    private EditText mEtcontent;

    private int mInputType;

    @Override
    public void onInitViews(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mTvName = IViewCompat.findById(this, R.id.bd_tv_name);
        mEtcontent = IViewCompat.findById(this, R.id.bd_et_content);
    }

    @Override
    public void onInitDatas(@NonNull Bundle arguments, Bundle savedInstanceState) {
        Uri data = getIntent().getData();
        if (data == null) {
            return;
        }
        setTitle(data.getQueryParameter(BDKey.KEY_TITLE));
        mInputType = BDUtils.valueOf(data.getQueryParameter(BDKey.KEY_INPUT_TYPE), InputType.TYPE_CLASS_TEXT);
        mEtcontent.setInputType(mInputType);
        if (mInputType == InputType.TYPE_CLASS_PHONE) {
            mEtcontent.setFilters(BDUtils.getPhoneFilters());
        }
        mTvName.setText(data.getQueryParameter(BDKey.KEY_NAME));
        mEtcontent.setText(data.getQueryParameter(BDKey.KEY_CONTENT));
        mEtcontent.setHint(data.getQueryParameter(BDKey.KEY_HINT));
        mEtcontent.setSingleLine(false);
        mEtcontent.setSelection(mEtcontent.length());
    }

    @Override
    public void onInitListeners(@NonNull Bundle arguments, Bundle savedInstanceState) {
        findViewById(R.id.bd_btn_confirm).setOnClickListener(this::onClickConfirm);
    }

    @Override
    public BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_edit_text);
    }

    public void onClickConfirm(View view) {
        String content = getContent();
        boolean textCallNull = IURIUtils.getQueryParameter(this, BDKey.KEY_TEXT_CAN_NULL, true);
        if (!textCallNull && TextUtils.isEmpty(content)) {
            showText(mEtcontent.getHint());
            return;
        }
        if (mInputType == InputType.TYPE_CLASS_PHONE) {
            if (!TextUtils.isEmpty(content) && !IAppUtils.isMobileNO(content)) {
                showText(R.string.bd_prompt_phone_format_error);
                return;
            }
        }
        if (mInputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            if (!TextUtils.isEmpty(content) && !IAppUtils.isEmail(content)) {
                showText(R.string.bd_prompt_email_format_error);
                return;
            }
        }
        Intent intent = new Intent();
        intent.putExtra(BDKey.KEY_CONTENT, content);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected String getContent() {
        return mEtcontent.getText().toString().trim();
    }
}
