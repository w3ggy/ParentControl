package com.abramov.artyom.parentcontrol.injection;

import com.abramov.artyom.parentcontrol.services.DataService;
import com.abramov.artyom.parentcontrol.ui.BaseFragment;
import com.abramov.artyom.parentcontrol.ui.main_screen.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class})
public interface Injector {
    void inject(MainActivity activity);
    void inject(DataService dataService);
    void inject(BaseFragment fragment);
    // void createInjector(MyFragment fragment);
    // void createInjector(MyService service);
}
