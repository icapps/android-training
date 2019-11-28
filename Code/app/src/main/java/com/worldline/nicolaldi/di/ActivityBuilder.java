package com.worldline.nicolaldi.di;

import com.worldline.nicolaldi.MainActivity;
import com.worldline.nicolaldi.service.BoundTransactionSaverService;
import com.worldline.nicolaldi.service.TransactionSaverService;
import com.worldline.nicolaldi.service.TransactionSaverServiceNormal;
import com.worldline.nicolaldi.service.TransactionSenderService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Nicola Verbeeck
 */
@Module
public interface ActivityBuilder {

    @ContributesAndroidInjector
    MainActivity bindMainActivity();

    @ContributesAndroidInjector
    BoundTransactionSaverService bindBoundSaverService();

    @ContributesAndroidInjector
    TransactionSaverService bindSaverService();

    @ContributesAndroidInjector
    TransactionSenderService bindSenderService();

    @ContributesAndroidInjector
    TransactionSaverServiceNormal bindNormalSaverService();

}
