package com.worldline.nicolaldi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.util.TransactionSaver;

/**
 * @author Nicola Verbeeck
 */
public class TransactionSaverServiceNormal extends Service {

    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_LOCATION = "location";

    public static Intent createIntent(Context context, double amount, Location location) {
        Intent intent = new Intent(context, TransactionSaverServiceNormal.class);
        intent.putExtra(EXTRA_AMOUNT, amount);
        intent.putExtra(EXTRA_LOCATION, location);
        return intent;
    }

    private TransactionSaver saver;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        saver = new TransactionSaver(this);
        Log.d("TransactionNormal", "Service is being crearted :)");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TransactionNormal", "Service is dying :(");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TransactionNormal", "Service is executing command");

        handler.removeCallbacksAndMessages(null);

        final double amount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0);
        final Location location = intent.getParcelableExtra(EXTRA_LOCATION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                saver.saveTransaction(amount, location);
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 10000L);

        return START_NOT_STICKY;
    }

}
