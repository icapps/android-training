package com.worldline.nicolaldi.util;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Nicola Verbeeck
 */
@Singleton
public class TransactionSaver {

    private final Context applicationContext;

    @Inject
    public TransactionSaver(Application context) {
        applicationContext = context.getApplicationContext();
    }

    public void saveTransaction(double totalAmount, @Nullable Location location) {
        synchronized (this) {
            long timestamp = System.currentTimeMillis();

            String transactionLogItem = timestamp + " = " + totalAmount;
            if (location != null)
                transactionLogItem += " @ " + location.toString();

            transactionLogItem += "\n";

            Log.d("TransactionSaver", "Saving transaction: " + transactionLogItem);

            try {
                OutputStream outputStream = applicationContext.openFileOutput("transaction.log", Context.MODE_APPEND);
                outputStream.write(transactionLogItem.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTransactionLog() {
        synchronized (this) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(applicationContext.openFileInput("transaction.log")))) {
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    result.append(line).append('\n');
                }
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

}
