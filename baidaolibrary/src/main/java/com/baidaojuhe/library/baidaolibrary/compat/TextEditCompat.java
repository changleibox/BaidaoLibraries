/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants.BDKey;

import net.box.app.library.IContext;
import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IURIUtils;

import java.util.List;

import static net.box.app.library.util.IURIUtils.createUriIntent;

/**
 * Created by box on 2017/4/17.
 * <p>
 * 兼容文本编辑
 */

@SuppressWarnings("WeakerAccess")
public class TextEditCompat {

    private static final String URL_EDIT_TEXT = "edit://" + IAppHelper.getContext().getPackageName();

    public static final int REQUEST_CODE_EDIT_TEXT = BDConstants.BDRequestCode.REQUEST_CODE_EDIT_TEXT;

    public static final int REQUEST_CODE_EDIT_IMAGE_TEXT = BDConstants.BDRequestCode.REQUEST_CODE_EDIT_IMAGE_TEXT;

    /**
     * @see #editText(IContext, int, int, String, int, int)
     * @see .activity.EditTextActivity
     */
    public static void editText(@NonNull IContext iContext, @StringRes int title, @StringRes int name, @Nullable String content, @StringRes int hint) {
        editText(iContext, title, name, content, hint, InputType.TYPE_CLASS_TEXT);
    }

    /**
     * @see TextEditCompat#editText(IContext, String, String, String, String, int)
     */
    public static void editText(@NonNull IContext iContext, @StringRes int title, @StringRes int name, @Nullable String content, @StringRes int hint, int inputType) {
        editText(iContext, IAppHelper.getString(title), IAppHelper.getString(name), content, IAppHelper.getString(hint), inputType);
    }

    public static void editText(@NonNull IContext iContext, @StringRes int title, @StringRes int name, @Nullable String content, @StringRes int hint, int inputType, boolean canTextNull) {
        editText(iContext, IAppHelper.getString(title), IAppHelper.getString(name), content, IAppHelper.getString(hint), inputType, canTextNull);
    }

    /**
     * 编辑文本
     *
     * @param iContext  上下文环境
     * @param title     页面标题
     * @param name      左边的文字
     * @param content   需要显示的文本
     * @param hint      提示
     * @param inputType {@link InputType}
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.EditTextActivity
     */
    public static void editText(@NonNull IContext iContext, @NonNull String title, @NonNull String name, @Nullable String content, @NonNull String hint, int inputType) {
        editText(iContext, title, name, content, hint, inputType, true);
    }

    /**
     * 编辑文本
     *
     * @param iContext    上下文环境
     * @param title       页面标题
     * @param name        左边的文字
     * @param content     需要显示的文本
     * @param hint        提示
     * @param inputType   {@link InputType}
     * @param canTextNull content是否可以为空
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.EditTextActivity
     */
    public static void editText(@NonNull IContext iContext, @NonNull String title, @NonNull String name, @Nullable String content, @NonNull String hint, int inputType, boolean canTextNull) {
        IURIUtils.UrlCreator creator = new IURIUtils.UrlCreator(URL_EDIT_TEXT, "text");
        creator.put(BDKey.KEY_TITLE, title);
        creator.put(BDKey.KEY_NAME, name);
        creator.put(BDKey.KEY_CONTENT, content);
        creator.put(BDKey.KEY_HINT, hint);
        creator.put(BDKey.KEY_INPUT_TYPE, inputType);
        creator.put(BDKey.KEY_TEXT_CAN_NULL, canTextNull);
        iContext.startActivityForResult(createUriIntent(creator.toString()), REQUEST_CODE_EDIT_TEXT);
    }

    /**
     * @see #editImageText(IContext, int, String, int, int, boolean)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @StringRes int title, @Nullable String content, @StringRes int hint, boolean hasImage) {
        editImageText(iContext, title, content, hint, Integer.valueOf(InputType.TYPE_CLASS_TEXT), hasImage);
    }

    /**
     * @see #editImageText(IContext, int, String, int, int, Integer, int, boolean)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @StringRes int title, @Nullable String content, @StringRes int hint, @IntRange(from = -1) int textCount, boolean hasImage) {
        editImageText(iContext, title, content, hint, R.string.box_btn_sure, InputType.TYPE_CLASS_TEXT, textCount, hasImage);
    }

    /**
     * 编辑图文
     *
     * @param iContext  上下文环境
     * @param title     页面标题
     * @param content   需要显示的文本
     * @param hint      提示
     * @param inputType {@link InputType}
     * @param hasImage  是否可以选择图片
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @StringRes int title, @Nullable String content, @StringRes int hint, @NonNull Integer inputType, boolean hasImage) {
        editImageText(iContext, title, content, hint, R.string.box_btn_sure, inputType, hasImage);
    }

    /**
     * 编辑图文
     *
     * @param iContext    上下文环境
     * @param title       页面标题
     * @param content     需要显示的文本
     * @param hint        提示
     * @param confirmText 确认按钮的文本
     * @param inputType   {@link InputType}
     * @param hasImage    是否可以选择图片
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @StringRes int title, @Nullable String content,
                                     @StringRes int hint, @StringRes int confirmText, @NonNull Integer inputType, boolean hasImage) {
        editImageText(iContext, title, content, hint, confirmText, inputType, -1, hasImage);
    }

    /**
     * 编辑图文
     *
     * @param iContext    上下文环境
     * @param title       页面标题
     * @param content     需要显示的文本
     * @param hint        提示
     * @param confirmText 确认按钮的文本
     * @param inputType   {@link InputType}
     * @param textCount   字数限制，-1就是无限制
     * @param hasImage    是否可以选择图片
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @StringRes int title, @Nullable String content,
                                     @StringRes int hint, @StringRes int confirmText, @NonNull Integer inputType, @IntRange(from = -1) int textCount, boolean hasImage) {
        editImageText(iContext, IAppHelper.getString(title), content, IAppHelper.getString(hint), IAppHelper.getString(confirmText), inputType, textCount, hasImage);
    }

    /**
     * 编辑图文
     *
     * @param iContext    上下文环境
     * @param title       页面标题
     * @param content     需要显示的文本
     * @param hint        提示
     * @param confirmText 确认按钮的文本
     * @param inputType   {@link InputType}
     * @param textCount   字数限制，-1就是无限制
     * @param hasImage    是否可以选择图片
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @NonNull String title, @Nullable String content,
                                     @NonNull String hint, @NonNull String confirmText, @NonNull Integer inputType, @IntRange(from = -1) int textCount, boolean hasImage) {
        editImageText(iContext, title, content, hint, confirmText, inputType, textCount, hasImage, true);
    }

    /**
     * 编辑图文
     *
     * @param iContext    上下文环境
     * @param title       页面标题
     * @param content     需要显示的文本
     * @param hint        提示
     * @param confirmText 确认按钮的文本
     * @param inputType   {@link InputType}
     * @param textCount   字数限制，-1就是无限制
     * @param hasImage    是否可以选择图片
     * @param canTextNull content是否可以为空
     * @see android.widget.EditText#setInputType(int)
     * @see .activity.ImageTextEditActivity
     */
    public static void editImageText(@NonNull IContext iContext, @NonNull String title, @Nullable String content,
                                     @NonNull String hint, @NonNull String confirmText, @NonNull Integer inputType, @IntRange(from = -1) int textCount, boolean hasImage, boolean canTextNull) {
        IURIUtils.UrlCreator creator = new IURIUtils.UrlCreator(URL_EDIT_TEXT, "imagetext");
        creator.put(BDKey.KEY_TITLE, title);
        creator.put(BDKey.KEY_CONTENT, content);
        creator.put(BDKey.KEY_HINT, hint);
        creator.put(BDKey.KEY_CONFIRM_TEXT, confirmText);
        creator.put(BDKey.KEY_INPUT_TYPE, inputType);
        creator.put(BDKey.KEY_HAS_IMAGE, hasImage);
        creator.put(BDKey.KEY_TEXT_LIMIT, textCount);
        creator.put(BDKey.KEY_TEXT_CAN_NULL, canTextNull);
        iContext.startActivityForResult(createUriIntent(creator.toString()), REQUEST_CODE_EDIT_IMAGE_TEXT);
    }

    public static void editRemakrs(@NonNull IContext iContext, @Nullable String content, boolean hasImage) {
        editImageText(iContext, R.string.bd_title_remark, content, R.string.bd_hint_please_input_remarks, BDConstants.BDConfig.SIZE_REMARKS, hasImage);
    }

    @Nullable
    public static String getText(Intent data) {
        return data == null ? null : data.getStringExtra(BDKey.KEY_CONTENT);
    }

    @Nullable
    public static List<String> getImages(Intent data) {
        return data == null ? null : data.getStringArrayListExtra(BDKey.KEY_IMAGES);
    }

}
