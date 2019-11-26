package com.worldline.nicolaldi.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.util.TransactionSaver;

/**
 * @author Nicola Verbeeck
 */
public class TransactionSaverService extends IntentService {

    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_LOCATION = "location";

    public static Intent createIntent(Context context, double amount, Location location){
        Intent intent = new Intent(context, TransactionSaverService.class);
        intent.putExtra(EXTRA_AMOUNT, amount);
        intent.putExtra(EXTRA_LOCATION, location);
        return intent;
    }

    private TransactionSaver saver;

    public TransactionSaverService() {
        super("TransactionSaver");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        saver = new TransactionSaver(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;

        if (Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("Not on main thread");

        double amount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0);
        Location location = intent.getParcelableExtra(EXTRA_LOCATION);
        saver.saveTransaction(amount, location);
    }
}
