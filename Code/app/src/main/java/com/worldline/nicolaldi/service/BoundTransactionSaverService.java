package com.worldline.nicolaldi.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.worldline.nicolaldi.MyApplication;
import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.util.TransactionSaver;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.android.DaggerService;

/**
 * @author Nicola Verbeeck
 */
public class BoundTransactionSaverService extends DaggerService {

    @Inject
    TransactionSaver transactionSaver;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("BoundService", "Created!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentTitle("Service");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateWithChannel(builder);
        }

        startForeground(10, builder
                .build());

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateWithChannel(Notification.Builder builder) {
        final NotificationChannel notificationChannel = new NotificationChannel("FGChannel",
                "FG Channel Name", NotificationManager.IMPORTANCE_NONE);

        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);

        builder.setChannelId("FGChannel");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("BoundService", "Destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BoundService", "Bound!");
        return new TransactionSaverBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void saveTransaction(final double amount, final Location location) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                transactionSaver.saveTransaction(amount, location);
            }
        });
    }

    public static class TransactionSaverBinder extends Binder {

        private final BoundTransactionSaverService serviceInstance;

        public TransactionSaverBinder(BoundTransactionSaverService serviceInstance) {
            this.serviceInstance = serviceInstance;
        }

        public void saveTransaction(double amount, Location location) {
            serviceInstance.saveTransaction(amount, location);
        }

    }
}
