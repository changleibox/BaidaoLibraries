/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by box on 2017/9/21.
 * <p>
 * 到访问卷
 */

@SuppressWarnings("unused")
public class NaireQuestion implements Parcelable {

    /**
     * id : 1
     * parentId : 0
     * questionName : 需求户型
     * type : 1
     * choiceType : 1
     * createDate : 2017-09-11 09:48:37
     * updateDate :
     * delTag : 0
     * questions : []
     */

    private int id;
    private int parentId;
    private String questionName;
    private List<NaireQuestion> questions;
    private Serializable tag;

    public NaireQuestion(int id, String questionName) {
        this.id = id;
        this.questionName = questionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<NaireQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<NaireQuestion> questions) {
        this.questions = questions;
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getTag() {
        return (T) tag;
    }

    public void setTag(Serializable tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        // return obj != null && obj instanceof NaireQuestion
        //         && (super.equals(obj) || (((NaireQuestion) obj).getId() == id
        //         && ((NaireQuestion) obj).getParentId() == parentId) || );
        return obj != null && obj instanceof NaireQuestion && ((NaireQuestion) obj).getQuestionName().equals(questionName);
    }

    public NaireQuestion() {
    }

    public NaireQuestion(String questionName) {
        this.questionName = questionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.parentId);
        dest.writeString(this.questionName);
        dest.writeTypedList(this.questions);
        dest.writeSerializable(this.tag);
    }

    protected NaireQuestion(Parcel in) {
        this.id = in.readInt();
        this.parentId = in.readInt();
        this.questionName = in.readString();
        this.questions = in.createTypedArrayList(NaireQuestion.CREATOR);
        this.tag = in.readSerializable();
    }

    public static final Creator<NaireQuestion> CREATOR = new Creator<NaireQuestion>() {
        @Override
        public NaireQuestion createFromParcel(Parcel source) {
            return new NaireQuestion(source);
        }

        @Override
        public NaireQuestion[] newArray(int size) {
            return new NaireQuestion[size];
        }
    };
}
