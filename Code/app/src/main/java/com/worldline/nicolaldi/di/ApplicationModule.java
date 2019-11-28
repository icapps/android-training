package com.worldline.nicolaldi.di;

import android.app.Application;

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

}
