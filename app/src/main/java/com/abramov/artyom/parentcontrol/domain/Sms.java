package com.abramov.artyom.parentcontrol.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sms extends RealmObject {
    @PrimaryKey
    private String mId;
    private String mAuthor;
    private String mMessage;

    public Sms() {

    }

    public Sms(String id, String author, String message) {
        mId = id;
        mAuthor = author;
        mMessage = message;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
