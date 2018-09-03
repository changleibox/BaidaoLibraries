/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.common;

import net.box.app.library.helper.IAppHelper;

/**
 * Created by box on 2017/11/22.
 * <p>
 * 常量
 */

public final class BDConstants {

    public static final String BD_BAR = "-";
    public static final String BD_SPACE = " ";

    public interface BDConfig {
        int FIRST_PAGE = 1;
        int SIZE_REMARKS = 200;
    }

    public interface BDKey {
        String KEY_TITLE = "title";
        String KEY_HINT = "hint";
        String KEY_CONTENT = "content";
        String KEY_CONFIRM_TEXT = "confirmText";
        String KEY_INPUT_TYPE = "inputType";
        String KEY_TEXT_CAN_NULL = "textCallNull";
        String KEY_HAS_IMAGE = "hasImage";
        String KEY_IMAGES = "images";
        String KEY_TEXT_LIMIT = "textLimit";
        String KEY_NAME = "name";
        String KEY_REQUEST_TYPE = "REQUEST_TYPE";
        String KEY_FONT_CAMERA = "FONT_CAMERA";
        String KEY_VIDEO_URL = "VIDEO_URL";
        String KEY_IMAGE_PATHS = "IMAGE_PATHS";
        String KEY_CURRENT_POSITION = "CURRENT_POSITION";
        String KEY_IS_MODALITY = "isModality";
        String KEY_RESULT_PICTURE_POSITION = "resultPicturePosition";
        String KEY_PRESET_ANSWERS = "presetAnswers";
        String KEY_SELECTED_ANSWERS = "selectedAnswers";
        String KEY_SINGLE_SELECTION = "singleSelection";
        String KEY_CONTENT_TYPE = "contentType";
    }

    public interface BDRequestCode {
        int REQUEST_CODE_CAMERA = 0x1;
        int REQUEST_CODE_ALBUM = 0x2;
        int RESUEST_CODE_DOWNLOAD_FILE = 0x3;
        int RESUEST_CODE_PREVIEW_IMAGE = 0x4;
        int REQUEST_CODE_EDIT_TEXT = 0x5;
        int REQUEST_CODE_EDIT_IMAGE_TEXT = 0x6;
    }

    public interface BDTransitionName {
        String TRANSITION_NAME_SEARCH = "transitionNameSearch";
        String TRANSITION_NAME_PREVIEW = "transitionNameImagePreview";
    }

    public interface BDIntentAction {
        String ACTION_PREVIEW = IAppHelper.getContext().getPackageName() + ".action.PREVIEW";
        String ACTION_VIDEO = IAppHelper.getContext().getPackageName() + ".action.VIDEO";
    }

}
