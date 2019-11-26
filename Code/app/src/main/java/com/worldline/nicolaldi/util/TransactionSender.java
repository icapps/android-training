package com.worldline.nicolaldi.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.worldline.nicolaldi.service.NicolaldiService;

import java.io.IOException;

/**
 * @author Nicola Verbeeck
 */
public class TransactionSender {

    private final NicolaldiService webService;
    private final TransactionSaver transactionSaver;

    public TransactionSender(TransactionSaver saver, NicolaldiService service) {
        transactionSaver = saver;
        webService = service;
    }

    @WorkerThread
    public synchronized void sendTransactions() {
        Log.d("TransactionSender", "Sending transaction log");

        final String transactionLog = transactionSaver.getTransactionLog();

        try {
            webService.sendTransactionLog(transactionLog).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
