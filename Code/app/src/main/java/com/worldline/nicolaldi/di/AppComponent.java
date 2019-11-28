package com.worldline.nicolaldi.di;

import com.worldline.nicolaldi.MainActivity;
import com.worldline.nicolaldi.MyApplication;
import com.worldline.nicolaldi.util.TransactionSender;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Nicola Verbeeck
 */
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class
})
@Singleton
public interface AppComponent extends AndroidInjector<MyApplication> {

    void inject(TransactionSender sender);

}
