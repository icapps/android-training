package com.worldline.nicolaldi.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.MyApplication;
import com.worldline.nicolaldi.util.TransactionSender;

/**
 * @author Nicola Verbeeck
 */
public class TransactionSenderService extends IntentService {

    private TransactionSender transactionSender;

    public TransactionSenderService() {
        super("TransactionSender");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        transactionSender = ((MyApplication)this.getApplication()).transactionSender;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TransactionSenderServ", "Fired intent!");

        transactionSender.sendTransactions();
    }
}
