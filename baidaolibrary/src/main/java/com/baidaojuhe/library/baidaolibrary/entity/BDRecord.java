/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by box on 2017/5/19.
 * <p>
 * 历史记录
 */

public class BDRecord implements Parcelable {

    private String recordName;

    public BDRecord() {
    }

    public BDRecord(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    @Override
    public String toString() {
        return recordName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recordName);
    }

    protected BDRecord(Parcel in) {
        this.recordName = in.readString();
    }

    public static final Parcelable.Creator<BDRecord> CREATOR = new Parcelable.Creator<BDRecord>() {
        @Override
        public BDRecord createFromParcel(Parcel source) {
            return new BDRecord(source);
        }

        @Override
        public BDRecord[] newArray(int size) {
            return new BDRecord[size];
        }
    };
}
