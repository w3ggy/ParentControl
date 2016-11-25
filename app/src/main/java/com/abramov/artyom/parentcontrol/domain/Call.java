package com.abramov.artyom.parentcontrol.domain;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Call extends RealmObject {
    @PrimaryKey
    private String mId;
    private String mName;
    private String mNumber;
    private String mDuration;
    private Date mDate;

    public Call() {
    }

    public Call(String name, String number, String duration, Long date, String id) {
        mName = name;
        mNumber = number;
        mDuration = duration;
        mDate = new Date(date);
        mId = id;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
