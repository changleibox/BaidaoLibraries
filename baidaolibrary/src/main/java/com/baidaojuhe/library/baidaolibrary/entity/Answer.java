/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by box on 2018/5/2.
 * <p>
 * 到访问卷答案
 */
@SuppressWarnings("unused")
public class Answer implements Parcelable {

    /**
     * id : 1
     * parentId : 0
     * content : 高层
     * list : [{"id":5,"parentId":"1","content":"四居"}]
     */

    private String id;
    private String parentId;
    private String content;
    @SerializedName("list")
    private List<Answer> childs;

    private String tmpContent;

    public Answer(String content) {
        this.content = content;
    }

    public Answer(NaireQuestion question) {
        String questionName = question.getQuestionName();
        String[] names = questionName.split("-");
        this.content = names.length > 1 ? names[1] : names[0];
        this.tmpContent = questionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getChilds() {
        return childs;
    }

    public void setChilds(List<Answer> childs) {
        this.childs = childs;
    }

    public String getTmpContent() {
        return TextUtils.isEmpty(tmpContent) ? content : tmpContent;
    }

    public void setTmpContent(String tmpContent) {
        this.tmpContent = tmpContent;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Answer && (id != null && (id.equals(((Answer) obj).id)) || TextUtils.equals(((Answer) obj).getTmpContent(), getTmpContent()));
    }

    public Answer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.parentId);
        dest.writeString(this.content);
        dest.writeTypedList(this.childs);
        dest.writeString(this.tmpContent);
    }

    protected Answer(Parcel in) {
        this.id = in.readString();
        this.parentId = in.readString();
        this.content = in.readString();
        this.childs = in.createTypedArrayList(Answer.CREATOR);
        this.tmpContent = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
