package com.worldline.nicolaldi.di;

import android.app.Application;

import com.worldline.nicolaldi.service.NicolaldiService;
import com.worldline.nicolaldi.util.TransactionSaver;
import com.worldline.nicolaldi.util.TransactionSender;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Nicola Verbeeck
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public TransactionSaver provideTransactionSaver() {
        return new TransactionSaver(application);
    }

}
