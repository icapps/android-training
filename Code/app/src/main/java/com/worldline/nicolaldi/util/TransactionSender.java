package com.worldline.nicolaldi.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.worldline.nicolaldi.service.NicolaldiService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 * @author Nicola Verbeeck
 */
@Singleton
public class TransactionSender {

    private final NicolaldiService webService;
    private final Lazy<TransactionSaver> transactionSaver;

    @Inject
    public TransactionSender(Lazy<TransactionSaver> saver, NicolaldiService service) {
        transactionSaver = saver;
        webService = service;
    }

    @WorkerThread
    public synchronized void sendTransactions() {
        Log.d("TransactionSender", "Sending transaction log");

        final String transactionLog = transactionSaver.get().getTransactionLog();

        try {
            webService.sendTransactionLog(transactionLog).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
