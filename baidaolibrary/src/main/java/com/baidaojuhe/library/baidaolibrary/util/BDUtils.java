/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.common.BDConstants;
import com.baidaojuhe.library.baidaolibrary.compat.ToastCompat;

import net.box.app.library.IAppCompatActivity;
import net.box.app.library.IContext;
import net.box.app.library.common.IConstants;
import net.box.app.library.common.IConstants.IRequestCode;
import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IPermissionCompat;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static net.box.app.library.common.IConstants.ISize.LIMIT_PHONE_LENGTH;

/**
 * Created by box on 2017/8/16.
 * <p>
 * 工具
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public final class BDUtils {

    public static String getAppName() {
        Context context = IAppHelper.getContext();
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    public static void startWechat(IContext activity) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            if (isIntentAvaileble(activity.getContext(), intent)) {
                activity.startActivity(intent);
            } else {
                activity.showText(R.string.bd_prompt_wechat_client_inavailable);
            }
        } catch (Exception e) {
            activity.showText(R.string.bd_prompt_wechat_client_inavailable);
        }
    }

    public static boolean isIntentAvaileble(Context context, Intent intent) {
        List<?> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolves.size() > 0;
    }

    /**
     * 截图
     */
    public static void startPhotoZoom(Activity activity, Uri uri, File output, int requestCode) throws Exception {
        try {
            Intent intentPic = new Intent("com.android.camera.action.CROP");
            intentPic.setDataAndType(uri, "image/*");
            intentPic.putExtra("crop", "true");
            intentPic.putExtra("aspectX", 1);
            intentPic.putExtra("aspectY", 1);
            intentPic.putExtra("outputX", 80);
            intentPic.putExtra("outputY", 80);
            intentPic.putExtra("return-data", true);
            intentPic.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intentPic.putExtra("noFaceDetection", false);
            intentPic.putExtra("scale", true);
            intentPic.putExtra("output", Uri.fromFile(output));
            activity.startActivityForResult(intentPic, requestCode);
        } catch (Exception ex) {
            ToastCompat.showText(R.string.box_lable_no_sdcard);
            ex.printStackTrace();
            throw ex;
        }
    }

    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public static void setSuperPrivateFieldValue(Object instance, String variableName, Object value) {
        try {
            Class targetClass = instance.getClass().getSuperclass();
            Field personNameField = targetClass.getDeclaredField(variableName);
            personNameField.setAccessible(true);
            personNameField.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double valueOf(CharSequence value, @NonNull Double defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        double d = defaultValue;
        try {
            d = Double.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static long valueOf(CharSequence value, @NonNull Long defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        long d = defaultValue;
        try {
            d = Long.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static int valueOf(CharSequence value, @NonNull Integer defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        int d = defaultValue;
        try {
            d = Integer.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static List<Double> getNumbers(double start, double stop, double step) {
        ArrayList<Double> values = new ArrayList<>();
        for (double axis = start; axis <= stop; axis += step) {
            values.add(axis);
        }
        return values;
    }

    public static List<Integer> getNumbers(int start, int stop, int step) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int axis = start; axis <= stop; axis += step) {
            values.add(axis);
        }
        return values;
    }

    public static InputFilter[] getPhoneFilters() {
        return new InputFilter[]{getPhoneFilter(), new InputFilter.LengthFilter(LIMIT_PHONE_LENGTH)};
    }

    public static InputFilter getPhoneFilter() {
        return (source, start, end, dest, dstart, dend)
                -> TextUtils.isEmpty(source) ? null : source.toString()
                .replaceAll(BDConstants.BD_BAR, IConstants.EMPTY)
                .replaceAll(BDConstants.BD_SPACE, IConstants.EMPTY);
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static byte[] intToByte(int value) {
        byte[] data = new byte[4];
        data[3] = (byte) (value & 0xFF);
        data[2] = (byte) ((value >> 8) & 0xFF);
        data[1] = (byte) ((value >> 16) & 0xFF);
        data[0] = (byte) ((value >> 24) & 0xFF);
        return data;
    }

    public static int byteToInt(byte[] data) {
        return (data[3] & 0xFF) + ((data[2] & 0xFF) << 8) + ((data[1] & 0xFF) << 16) + ((data[0] & 0xFF) << 24);
    }

    public static void chooseFiles(Activity context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String title = context.getString(net.box.app.library.R.string.box_lable_choose_file);
        context.startActivityForResult(Intent.createChooser(intent, title), IRequestCode.REQUEST_CHOOSE_FILE);
    }

    public static void call(String phone, Context context, int requestCode) {
        if (IPermissionCompat.checkSelfPermission((IContext) context, requestCode, Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone.replaceAll("-", "")));
            context.startActivity(intent);
        }
    }
}
