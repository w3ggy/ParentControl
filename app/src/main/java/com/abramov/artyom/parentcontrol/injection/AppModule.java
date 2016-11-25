package com.abramov.artyom.parentcontrol.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abramov.artyom.parentcontrol.model.BaseModel;
import com.abramov.artyom.parentcontrol.net.NetService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Provides
    @Singleton
    BaseModel provideBaseModel() {
        return new BaseModel();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient client = new OkHttpClient();
/*        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(mApplication.getBaseContext().getCacheDir(), cacheSize);
        client.setCache(cache);*/

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(NetService.BASE_URL)
                .client(client)
                .build();
        return retrofit;
    }
}
