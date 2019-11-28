package com.worldline.nicolaldi.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.util.TransactionSender;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;

/**
 * @author Nicola Verbeeck
 */
public class TransactionSenderService extends DaggerIntentService {

    @Inject
    TransactionSender transactionSender;

    public TransactionSenderService() {
        super("TransactionSender");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TransactionSenderServ", "Fired intent!");

        transactionSender.sendTransactions();
    }
}
