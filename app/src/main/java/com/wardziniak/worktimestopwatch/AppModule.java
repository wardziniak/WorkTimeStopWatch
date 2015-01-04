package com.wardziniak.worktimestopwatch;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wardziniak on 1/2/15.
 */
@Module(
        injects = App.class,
        library = true
)
public class AppModule {

    private App app;

    private Bus eventBus;

    public AppModule(App app) {
        this.app = app;
        this.eventBus = new Bus();
    }

    @Provides
    public Application getApplication() {
        return  app;
    }

    @Singleton
    @Provides
    public Bus getEventBus() {
        return eventBus;
    }
}
