package com.abramov.artyom.parentcontrol.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject{
    @PrimaryKey
    private String mDeviceId;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;

    public Location(String title, String deviceId, double latitude, double longitude) {
        mTitle = title;
        mDeviceId = deviceId;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        mTitle = title;
    }
}
