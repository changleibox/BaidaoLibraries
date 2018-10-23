/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;
import com.baidaojuhe.library.baidaolibrary.util.BDLayout;
import com.baidaojuhe.library.baidaolibrary.widget.PicturesView;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IURIUtils;

import java.util.ArrayList;

/**
 * Created by box on 2017/4/4.
 * <p>
 * 图文编辑
 */

@SuppressWarnings("unused")
public class BDImageTextEditActivity extends BDModalityActivity implements TextWatcher {

    private static final int MIN_LINES = 8;
    private static final int MAX_LINES = 12;
    private static final int TEXT_NOT_LIMIT = -1;

    private EditText mEtcontent;
    private TextView mTvCount;
    private PicturesView mPicturesView;
    private Button mBtnConfirm;

    private int mTextLimit;

    @Override
    public void onInitViews(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mEtcontent = IViewCompat.findById(this, R.id.bd_et_content);
        mTvCount = IViewCompat.findById(this, R.id.bd_tv_count);
        mPicturesView = IViewCompat.findById(this, R.id.bd_picture);
        mBtnConfirm = IViewCompat.findById(this, R.id.bd_btn_confirm);
    }

    @Override
    public void onInitDatas(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mEtcontent.addTextChangedListener(this);

        Uri data = getIntent().getData();
        if (data == null) {
            return;
        }

        mPicturesView.setTakePictureMode(PicturesView.TakePictureMode.ALL);
        mPicturesView.setEditable(true);
        mPicturesView.setVisibility(data.getBooleanQueryParameter(BDKey.KEY_HAS_IMAGE, true) ? View.VISIBLE : View.GONE);

        setTitle(data.getQueryParameter(BDKey.KEY_TITLE));
        String textLimitStr = data.getQueryParameter(BDKey.KEY_TEXT_LIMIT);
        mTextLimit = TEXT_NOT_LIMIT;
        try {
            mTextLimit = Integer.valueOf(textLimitStr);
        } catch (Exception ignored) {
        }
        if (mTextLimit == TEXT_NOT_LIMIT) {
            mTvCount.setVisibility(View.GONE);
        } else {
            mTvCount.setVisibility(View.VISIBLE);
            mEtcontent.setFilters(new InputFilter[]{new LengthFilter(mTextLimit)});
        }
        mEtcontent.setText(data.getQueryParameter(BDKey.KEY_CONTENT));
        mEtcontent.setHint(data.getQueryParameter(BDKey.KEY_HINT));
        String confirmText = data.getQueryParameter(BDKey.KEY_CONFIRM_TEXT);
        if (!TextUtils.isEmpty(confirmText)) {
            mBtnConfirm.setText(confirmText);
        }
        String inputTypeStr = data.getQueryParameter(BDKey.KEY_INPUT_TYPE);
        int inputType = InputType.TYPE_CLASS_TEXT;
        try {
            inputType = Integer.valueOf(inputTypeStr);
        } catch (Exception ignored) {
        }
        mEtcontent.setInputType(inputType);
        mEtcontent.setMinLines(MIN_LINES);
        mEtcontent.setSingleLine(false);
        mEtcontent.setMaxHeight((int) IAppHelper.applyDimensionDp(getResources().getConfiguration().screenHeightDp / 2));
        mEtcontent.setSelection(mEtcontent.length());
    }

    @Override
    public void onInitListeners(@NonNull Bundle arguments, Bundle savedInstanceState) {
        mBtnConfirm.setOnClickListener(this::onClickConfirm);
    }

    @Override
    public BDLayout getContainerLayout(@NonNull Bundle arguments, @Nullable Bundle savedInstanceState) {
        return BDLayout.create(R.layout.bd_activity_image_text_edit);
    }

    public void onClickConfirm(View view) {
        String content = getContent();
        boolean textCallNull = IURIUtils.getQueryParameter(this, BDKey.KEY_TEXT_CAN_NULL, true);
        if (!textCallNull && TextUtils.isEmpty(getContent())) {
            showText(mEtcontent.getHint());
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(BDKey.KEY_IMAGES, getImages());
        intent.putExtra(BDKey.KEY_CONTENT, getContent());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvCount.setText(String.format("%1$s/%2$s", TextUtils.isEmpty(s) ? 0 : s.toString().trim().length(), mTextLimit));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    protected String getContent() {
        return mEtcontent.getText().toString().trim();
    }

    protected ArrayList<String> getImages() {
        return new ArrayList<>(mPicturesView.getPicturePaths());
    }

    protected void setImageLimit(int limit) {
        mPicturesView.setLimit(limit);
    }

    protected void setOnFileCallback(PicturesView.Callback fileCallback) {
        mPicturesView.setOnFileCallback(fileCallback);
    }

    protected void setOnFileRemoveCallback(PicturesView.RemoveCallback removeCallback) {
        mPicturesView.setOnFileRemoveCallback(removeCallback);
    }

    protected PicturesView getPicturesView() {
        return mPicturesView;
    }
}
