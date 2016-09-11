package com.abramov.artyom.parentcontrol.injection;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class})
public interface Injector {
    void inject(Activity activity);
    // void createInjector(MyFragment fragment);
    // void createInjector(MyService service);
}
