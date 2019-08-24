/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.R;

import net.box.app.library.compat.IAttrCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by box on 2017/3/27.
 * <p>
 * 可点击的切有提示和值的按钮
 */

@SuppressWarnings({"deprecation", "unused"})
public class ItemButton extends LinearLayout {

    private static final int DEFAULT_TEXT_COLOR = 0xff333333;
    private static final int DEFAULT_DRAWABLE_PADDING = 10;
    private static final int DEFAULT_INTERVAl = 10;
    private static final int DEFAULT_TEXT_SIZE = 16;

    private static final int VALUE_START = 0;
    private static final int VALUE_END = 1;
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    @SuppressWarnings("unused")
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VALUE_START, VALUE_END})
    public @interface ValueGravity {
    }

    private TextView mTvPrompt;
    private TextView mTvValue;
    private ImageView mIvValueDrawble;
    // private OnClickListener mValueClickListener;

    public ItemButton(Context context) {
        this(context, null);
    }

    public ItemButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RtlHardcoded")
    public ItemButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(HORIZONTAL);
        super.setBaselineAligned(false);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemButton, defStyleAttr, 0);

        int defaultAppearance = IAttrCompat.getResourceId(context, android.R.attr.textAppearance, 0);
        int promptAppearance = a.getResourceId(R.styleable.ItemButton_promptTextAppearance, defaultAppearance);
        int valueAppearance = a.getResourceId(R.styleable.ItemButton_valueTextAppearance, defaultAppearance);

        final int promptDrawable = a.getResourceId(R.styleable.ItemButton_promptDrawable, 0);
        final int valueDrawable = a.getResourceId(R.styleable.ItemButton_valueDrawable, 0);

        int drawablePadding = a.getDimensionPixelSize(R.styleable.ItemButton_drawablePadding, DEFAULT_DRAWABLE_PADDING);
        int gravity = a.getInt(R.styleable.ItemButton_valueGravity, VALUE_END);
        int valueContentGravity = a.getInt(R.styleable.ItemButton_valueContentGravity, gravity);
        int interval = a.getDimensionPixelSize(R.styleable.ItemButton_interval, DEFAULT_INTERVAl);
        int maxlength = a.getInt(R.styleable.ItemButton_android_maxLength, -1);

        CharSequence hint = a.getText(R.styleable.ItemButton_android_hint);
        boolean isEditable = a.getBoolean(R.styleable.ItemButton_editable, false);

        mTvPrompt = new TextView(context);
        mTvPrompt.setSingleLine(false);
        mTvPrompt.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams promptParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // if (gravity == VALUE_START) {
        //     promptParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // } else {
        //     promptParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        // }
        promptParams.gravity = Gravity.CENTER_VERTICAL;
        addView(mTvPrompt, promptParams);

        if (isEditable) {
            mTvValue = new AppCompatEditText(context);
            mTvValue.setInputType(a.getInt(R.styleable.ItemButton_android_inputType, EditorInfo.TYPE_CLASS_TEXT));
            mTvValue.clearFocus();
            mTvValue.setFocusableInTouchMode(false);
        } else {
            mTvValue = new TextView(context);
        }
        int maxLines = a.getInt(R.styleable.ItemButton_android_maxLines, Integer.MAX_VALUE);
        int lines = a.getInt(R.styleable.ItemButton_android_lines, -1);
        if (maxLines <= 1) {
            mTvValue.setSingleLine(true);
        } else {
            mTvValue.setSingleLine(false);
            mTvValue.setMaxLines(maxLines);
            if (lines != -1) {
                mTvValue.setLines(lines);
            }
        }

        if (maxlength >= 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
        } else {
            setFilters(NO_FILTERS);
        }

        mTvValue.setEllipsize(TextUtils.TruncateAt.END);
        mTvValue.setGravity(Gravity.CENTER_VERTICAL | (valueContentGravity == VALUE_START ? GravityCompat.START : GravityCompat.END));

        LayoutParams valueParentParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        FrameLayout valueParent = new FrameLayout(context);
        addView(valueParent, valueParentParams);

        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(isEditable ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        valueParams.gravity = Gravity.CENTER_VERTICAL | (gravity == VALUE_START ? GravityCompat.START : GravityCompat.END);
        valueParams.leftMargin = interval;
        valueParent.addView(mTvValue, valueParams);

        mIvValueDrawble = new AppCompatImageView(context);
        mIvValueDrawble.setScaleType(ImageView.ScaleType.CENTER);
        mIvValueDrawble.setContentDescription(hint);
        mIvValueDrawble.setImageResource(valueDrawable);
        mIvValueDrawble.setPadding(drawablePadding, 0, 0, 0);

        LayoutParams valueDrawableParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        valueDrawableParams.gravity = Gravity.CENTER_VERTICAL;
        // valueDrawableParams.leftMargin = drawablePadding;
        addView(mIvValueDrawble, valueDrawableParams);

        // LayoutParams valueParams = new LayoutParams(gravity == VALUE_START ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // valueParams.gravity = Gravity.CENTER_VERTICAL;
        // valueParams.leftMargin = interval;
        // addView(mTvValue, valueParams);

        // mTvValue.setEnabled(isEditable);
        // if (isEditable) {
        //     mTvValue.setSingleLine(true);
        // }
        mTvValue.setFocusable(isEditable);
        mTvValue.setFocusableInTouchMode(isEditable);
        mTvValue.setBackgroundColor(Color.TRANSPARENT);
        mTvValue.setHint(TextUtils.isEmpty(hint) ? hint : Html.fromHtml(hint.toString().replaceAll("\n", "<br/>")));

        setPromptTextAppearance(promptAppearance);
        setValueTextAppearance(valueAppearance);

        setPromptDrawable(promptDrawable);
        setValueDrawable(valueDrawable);
        setDrawablePadding(drawablePadding);

        if (a.hasValue(R.styleable.ItemButton_promptTextColor)) {
            setPromptTextColor(a.getColorStateList(R.styleable.ItemButton_promptTextColor));
        }

        if (a.hasValue(R.styleable.ItemButton_valueTextColor)) {
            setValueTextColor(a.getColorStateList(R.styleable.ItemButton_valueTextColor));
        }

        if (a.hasValue(R.styleable.ItemButton_promptTextSize)) {
            setPromptTextSize(a.getDimensionPixelSize(R.styleable.ItemButton_promptTextSize, DEFAULT_TEXT_SIZE));
        }

        if (a.hasValue(R.styleable.ItemButton_valueTextSize)) {
            setValueTextSize(a.getDimensionPixelSize(R.styleable.ItemButton_valueTextSize, DEFAULT_TEXT_SIZE));
        }

        String prompt = a.getString(R.styleable.ItemButton_promptText);
        String value = a.getString(R.styleable.ItemButton_valueText);

        if (isInEditMode()) {
            setPromptText(TextUtils.isEmpty(prompt) ? "prompt" : Html.fromHtml(prompt.replaceAll("\n", "<br/>")));
            setValueText(TextUtils.isEmpty(value) && TextUtils.isEmpty(hint) ? "value" : !TextUtils.isEmpty(value) ? Html.fromHtml(value.replaceAll("\n", "<br/>")) : null);
        } else {
            setPromptText(TextUtils.isEmpty(prompt) ? null : Html.fromHtml(prompt.replaceAll("\n", "<br/>")));
            setValueText(TextUtils.isEmpty(value) ? null : Html.fromHtml(value.replaceAll("\n", "<br/>")));
        }

        a.recycle();

        mTvPrompt.setDuplicateParentStateEnabled(true);
        mTvValue.setDuplicateParentStateEnabled(true);
        mIvValueDrawble.setDuplicateParentStateEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTvPrompt.setEnabled(enabled);
        mTvValue.setEnabled(enabled);
        mIvValueDrawble.setEnabled(enabled);
    }

    @Override
    public void setOrientation(int orientation) {
    }

    public void setPromptText(@StringRes int prompt) {
        setPromptText(getContext().getText(prompt));
    }

    public void setPromptText(CharSequence prompt) {
        mTvPrompt.setText(prompt);
    }

    public void setValueText(@StringRes int value) {
        setValueText(getContext().getText(value));
    }

    public void setValueText(CharSequence value) {
        mTvValue.setText(value);
    }

    public void setPromptTextAppearance(@StyleRes int textAppearance) {
        mTvPrompt.setTextAppearance(getContext(), textAppearance);
    }

    public void setValueTextAppearance(@StyleRes int textAppearance) {
        mTvValue.setTextAppearance(getContext(), textAppearance);
    }

    public void setPromptTextColor(@ColorInt int color) {
        mTvPrompt.setTextColor(color);
    }

    public void setValueTextColor(@ColorInt int color) {
        mTvValue.setTextColor(color);
    }

    public void setPromptTextColor(@Nullable ColorStateList color) {
        if (color == null) {
            mTvPrompt.setTextColor(DEFAULT_TEXT_COLOR);
        } else {
            mTvPrompt.setTextColor(color);
        }
    }

    public void setValueTextColor(@Nullable ColorStateList color) {
        if (color == null) {
            mTvValue.setTextColor(DEFAULT_TEXT_COLOR);
        } else {
            mTvValue.setTextColor(color);
        }
    }

    public void setPromptTextSize(float size) {
        mTvPrompt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setValueTextSize(float size) {
        mTvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setPromptDrawable(Drawable promptDrawable) {
        mTvPrompt.setCompoundDrawables(promptDrawable, null, null, null);
    }

    public void setValueDrawable(Drawable valueDrawable) {
        // mTvValue.setCompoundDrawables(null, null, valueDrawable, null);
        if (valueDrawable == null) {
            mIvValueDrawble.setVisibility(GONE);
            return;
        }
        mIvValueDrawble.setVisibility(VISIBLE);
        mIvValueDrawble.setImageDrawable(valueDrawable);
    }

    public void setPromptDrawable(@DrawableRes int promptDrawable) {
        setPromptDrawable(promptDrawable == 0 ? null : getDrawable(promptDrawable));
    }

    public void setValueDrawable(@DrawableRes int valueDrawable) {
        setValueDrawable(valueDrawable == 0 ? null : getDrawable(valueDrawable));
    }

    public void setDrawablePadding(int drawablePadding) {
        mTvPrompt.setCompoundDrawablePadding(drawablePadding);
        // mTvValue.setCompoundDrawablePadding(drawablePadding);
        LayoutParams valueDrawableParams = (LayoutParams) mIvValueDrawble.getLayoutParams();
        // valueDrawableParams.leftMargin = drawablePadding;
        mIvValueDrawble.setLayoutParams(valueDrawableParams);

        mIvValueDrawble.setPadding(drawablePadding, 0, 0, 0);
    }

    public void setHint(CharSequence hint) {
        mTvValue.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        mTvValue.setHint(hint);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mTvValue.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        mTvValue.removeTextChangedListener(watcher);
    }

    public void setOnValueFocusChangedListener(OnFocusChangeListener l) {
        mTvValue.setOnFocusChangeListener(l);
    }


    public void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        mTvValue.setOnEditorActionListener(l);
    }

    public Editable getEditableText() {
        return mTvValue.getEditableText();
    }

    public CharSequence getValue() {
        return mTvValue.getText();
    }

    public CharSequence getPrompt() {
        return mTvPrompt.getText();
    }

    public int getPromptTextColor() {
        return mTvPrompt.getCurrentTextColor();
    }

    public int getValueTextColor() {
        return mTvValue.getCurrentTextColor();
    }

    public void setFilters(InputFilter[] filters) {
        mTvValue.setFilters(filters);
    }

    public void setKeyListener(KeyListener input) {
        mTvValue.setKeyListener(input);
    }

    public boolean performValueDrawableClick() {
        // final boolean result;
        // if (mValueClickListener != null) {
        //     mValueClickListener.onClick(this);
        //     result = true;
        // } else {
        //     result = false;
        // }
        return mIvValueDrawble.performClick();
    }

    public void setValueDrawableClickListener(OnClickListener listener) {
        mIvValueDrawble.setOnClickListener(listener);
        // if (mTvValue instanceof EditText) {
        //     return;
        // }
        // mTvValue.setEnabled(true);
        // this.mValueClickListener = listener;
        // mTvValue.setOnTouchListener((v, event) -> {
        //     Drawable drawable = mTvValue.getCompoundDrawables()[2];
        //     if (drawable == null) {
        //         return super.onTouchEvent(event);
        //     }
        //     float x = event.getX();
        //     int width = mTvValue.getWidth();
        //     boolean touchable = (x > (width - mTvValue.getTotalPaddingRight()) && (x < ((width - mTvValue.getPaddingRight()))));
        //     if (event.getAction() == MotionEvent.ACTION_UP) {
        //         if (touchable) {
        //             if (mValueClickListener != null) {
        //                 mValueClickListener.onClick(v);
        //             }
        //             return true;
        //         }
        //     }
        //
        //     if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //         if (touchable) {
        //             return true;
        //         }
        //     }
        //
        //     return super.onTouchEvent(event);
        // });
    }

    public boolean performValueClick() {
        return mTvValue.performClick();
    }

    public void setValueClickListener(OnClickListener listener) {
        mTvValue.setOnClickListener(listener);
    }

    private Drawable getDrawable(@DrawableRes int id) {
        if (id == 0) {
            return null;
        }
        Drawable imgOff = getResources().getDrawable(id);
        imgOff.setBounds(0, 0, imgOff.getMinimumWidth(), imgOff.getMinimumHeight());
        return imgOff;
    }
}
