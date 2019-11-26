package com.worldline.nicolaldi.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.util.TransactionSaver;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Nicola Verbeeck
 */
public class BoundTransactionSaverService extends Service {

    private TransactionSaver transactionSaver;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        transactionSaver = new TransactionSaver(this);

        Log.d("BoundService", "Created!");
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
