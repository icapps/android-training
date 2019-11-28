package com.worldline.nicolaldi;

import com.worldline.nicolaldi.di.AppComponent;
import com.worldline.nicolaldi.di.ApplicationModule;
import com.worldline.nicolaldi.di.DaggerAppComponent;
import com.worldline.nicolaldi.di.NetworkModule;
import com.worldline.nicolaldi.service.NicolaldiService;

import org.mockito.Mockito;

import retrofit2.Retrofit;

/**
 * @author Nicola Verbeeck
 */
public class TestApplication extends MyApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AppComponent applicationInjector() {
        return DaggerAppComponent.builder()
                .networkModule(new NetworkModule() {
                    @Override
                    public NicolaldiService provideNicoaldiService(Retrofit retrofit) {
                        return Mockito.mock(NicolaldiService.class);
                    }
                }).applicationModule(new ApplicationModule(this))
                .build();
    }
}
