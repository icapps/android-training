package com.worldline.nicolaldi;

import com.worldline.nicolaldi.util.TransactionSender;

import org.mockito.Mockito;

/**
 * @author Nicola Verbeeck
 */
public class TestApplication extends MyApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        transactionSender = Mockito.mock(TransactionSender.class);
    }
}
