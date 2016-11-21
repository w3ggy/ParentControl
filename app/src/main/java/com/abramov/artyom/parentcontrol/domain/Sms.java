package com.abramov.artyom.parentcontrol.domain;

import io.realm.RealmObject;

public class Sms extends RealmObject {
    private String mAuthor;
    private String mMessage;

    public Sms(String mAuthor, String mMessage) {
        this.mAuthor = mAuthor;
        this.mMessage = mMessage;
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
