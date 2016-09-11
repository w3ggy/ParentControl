package com.abramov.artyom.parentcontrol;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.abramov.artyom.parentcontrol.injection.AppModule;
import com.abramov.artyom.parentcontrol.injection.DaggerInjector;
import com.abramov.artyom.parentcontrol.injection.Injector;
import com.firebase.client.Firebase;

import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    private RealmConfiguration mRealmConfig;
    private Injector mInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        mRealmConfig = new RealmConfiguration.Builder(this).build();
        mInjector = DaggerInjector.builder()
                .appModule(new AppModule(getApplicationContext()))
//                .netModule(new NetModule("https://api.github.com"))
                .build();
        Firebase.setAndroidContext(this);
    }

    public Injector getInjector() {
        return mInjector;
    }

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(context);
    }
}
