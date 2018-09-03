/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import com.baidaojuhe.library.baidaolibrary.R;
import com.baidaojuhe.library.baidaolibrary.compat.DateFormatCompat;
import com.baidaojuhe.library.baidaolibrary.compat.IViewCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by box on 2017/12/7.
 * <p>
 * 日期选择
 */

public class DatePickerDialog extends ChooseDialog {

    private DatePicker mDatePicker;

    @Nullable
    private OnDateChangedListener mChangedListener;

    public DatePickerDialog(Context context) {
        super(context);
        setContentView(R.layout.bd_dialog_date_picker);
        mDatePicker = IViewCompat.findById(this, R.id.bd_date_picker);
        mBtnConfirm.setVisibility(View.VISIBLE);
        Calendar cal = Calendar.getInstance();
        mDatePicker.setMaxDate(cal.getTimeInMillis());
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        mChangedListener = listener;
    }

    public Date getDate(int year, int monthOfYear, int dayOfMonth) {
        return DateFormatCompat.parseYMD(String.format("%1$s-%2$s-%3$s", year, monthOfYear + 1, dayOfMonth));
    }

    public void updateDate(@Nullable Date date) {
        if (date == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        mDatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onConfirm(View v) {
        super.onConfirm(v);
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();
        if (mChangedListener != null) {
            mChangedListener.onDateChanged(DatePickerDialog.this, year, month, dayOfMonth);
        }
    }

    /**
     * The callback used to indicate the user changed the date.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateChanged(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth);
    }
}
