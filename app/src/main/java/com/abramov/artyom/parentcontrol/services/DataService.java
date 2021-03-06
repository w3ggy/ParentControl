package com.abramov.artyom.parentcontrol.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.abramov.artyom.parentcontrol.MyApplication;
import com.abramov.artyom.parentcontrol.domain.Call;
import com.abramov.artyom.parentcontrol.domain.Loc;
import com.abramov.artyom.parentcontrol.domain.Sms;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.model.BaseModel;
import com.abramov.artyom.parentcontrol.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DataService extends IntentService {

    public DataService() {
        super(DataService.class.getName());
    }

    public DataService(String name) {
        super(name);
    }

    @Inject
    BaseModel mBaseModel;

    private LocationManager mLocationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).getInjector().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseModel = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getAction()) {
            case Constants.ACTION_SMS:
                mBaseModel.saveItems(getSMS());
                break;

            case Constants.ACTION_CALLS:
                mBaseModel.saveItems(getCalls());
                break;

            case Constants.ACTION_LOCATION:
                mBaseModel.saveItem(getLocation());
                break;

            case Constants.ACTION_ALL_DATA:
                mBaseModel.saveItems(getCalls());
                mBaseModel.saveItems(getSMS());
                mBaseModel.saveItem(getLocation());
                break;
        }
    }

    public List<Sms> getSMS() {
        List<Sms> sms = new ArrayList<>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndexOrThrow("_id"));
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add(new Sms(id, address, body));
        }

        return sms;
    }

    public List<Call> getCalls() {
        List<Call> calls = new ArrayList<>();
        Uri allCalls = Uri.parse("content://call_log/calls");
        Cursor c = getContentResolver().query(allCalls, null, null, null, null);
        while (c.moveToNext()) {
            calls.add(new Call(
                    c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)),
                    c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)),
                    c.getString(c.getColumnIndex(CallLog.Calls.DURATION)),
                    Long.valueOf(c.getString(c.getColumnIndex(CallLog.Calls.DATE))),
                    c.getString(c.getColumnIndex(CallLog.Calls._ID))));
        }

        Collections.reverse(calls);

        return calls;
    }

    private Loc getLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new Loc();
        }

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new Loc(
                DeviceUtils.getDeviceId(DataService.this),
                DeviceUtils.getDeviceId(DataService.this),
                location.getLatitude(),
                location.getLongitude());
    }
}
