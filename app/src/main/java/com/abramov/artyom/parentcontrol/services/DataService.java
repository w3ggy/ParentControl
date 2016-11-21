package com.abramov.artyom.parentcontrol.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;

import com.abramov.artyom.parentcontrol.domain.Call;
import com.abramov.artyom.parentcontrol.domain.Sms;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class DataService extends IntentService {
    private BaseModel mBaseModel;

    public DataService() {
        super(DataService.class.getName());
    }

    public DataService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseModel = new BaseModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseModel.destroy();
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
        }
    }

    public List<Sms> getSMS(){
        List<Sms> sms = new ArrayList<>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add(new Sms(address, body));
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
                    c.getString(c.getColumnIndex(CallLog.Calls.DURATION))));
        }

        return calls;
    }
}
