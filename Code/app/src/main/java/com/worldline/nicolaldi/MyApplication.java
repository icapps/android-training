package com.worldline.nicolaldi;

import com.worldline.nicolaldi.di.AppComponent;
import com.worldline.nicolaldi.di.ApplicationModule;
import com.worldline.nicolaldi.di.DaggerAppComponent;
import com.worldline.nicolaldi.util.AlarmUtil;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * @author Nicola Verbeeck
 */
public class MyApplication extends DaggerApplication {

    public static AppComponent injector;

    @Override
    public void onCreate() {
        super.onCreate();

        AlarmUtil.scheduleUploadAlarms(this);
    }

    @Override
    protected AppComponent applicationInjector() {
        injector = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        return injector;
    }
}
