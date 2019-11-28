package com.worldline.nicolaldi.di;

import android.app.Application;

import com.icapps.niddler.core.AndroidNiddler;
import com.icapps.niddler.core.Niddler;
import com.icapps.niddler.interceptor.okhttp.NiddlerOkHttpInterceptor;
import com.worldline.nicolaldi.service.NicolaldiService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Nicola Verbeeck
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public NicolaldiService provideNicoaldiService(Retrofit retrofit) {
        return retrofit.create(NicolaldiService.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(Niddler niddler) {
        return new OkHttpClient.Builder()
                .addInterceptor(new NiddlerOkHttpInterceptor(niddler, "Test"))
                .build();
    }

    @Provides
    @Singleton
    public Niddler provideNiddler(Application application) {
        AndroidNiddler niddler = new AndroidNiddler.Builder()
                .setPort(0)
                .setNiddlerInformation(AndroidNiddler.fromApplication(application))
                .setCacheSize(4 * 1024 * 1024)
                .build();

        niddler.attachToApplication(application);
        return niddler;
    }

}
