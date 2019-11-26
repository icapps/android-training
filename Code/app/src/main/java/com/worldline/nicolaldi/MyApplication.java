package com.worldline.nicolaldi;

import android.app.Application;

import com.icapps.niddler.core.AndroidNiddler;
import com.icapps.niddler.interceptor.okhttp.NiddlerOkHttpInterceptor;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.worldline.nicolaldi.service.NicolaldiService;
import com.worldline.nicolaldi.util.AlarmUtil;
import com.worldline.nicolaldi.util.TransactionSaver;
import com.worldline.nicolaldi.util.TransactionSender;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Nicola Verbeeck
 */
public class MyApplication extends Application {

    OkHttpClient client;
    public NicolaldiService service;

    public TransactionSaver transactionSaver;
    public TransactionSender transactionSender;

    @Override
    public void onCreate() {
        super.onCreate();

        final AndroidNiddler build = new AndroidNiddler.Builder()
                .setPort(0)
                .setNiddlerInformation(AndroidNiddler.fromApplication(this))
                .setCacheSize(4 * 1024 * 1024)
                .build();

        build.attachToApplication(this);

        client = new OkHttpClient.Builder()
                .addInterceptor(new NiddlerOkHttpInterceptor(build, "Test"))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();

        service = retrofit.create(NicolaldiService.class);

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException e) {
            //Ignore
        }

        transactionSaver = new TransactionSaver(this);
        transactionSender = new TransactionSender(transactionSaver, service);

        AlarmUtil.scheduleUploadAlarms(this);
    }
}
