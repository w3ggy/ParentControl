package com.abramov.artyom.parentcontrol.domain;

import io.realm.RealmObject;

public class Call extends RealmObject {
    private String mName;
    private String mNumber;
    private String mDuration;

    public Call() {
    }

    public Call(String name, String number, String duration) {
        mName = name;
        mNumber = number;
        mDuration = duration;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }
}
