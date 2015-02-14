package com.wardziniak.worktimestopwatch;

import android.app.Application;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.provider.Settings;

import com.squareup.otto.Bus;
import com.wardziniak.worktimestopwatch.eventBus.MainThreadBus;
import com.wardziniak.worktimestopwatch.workers.WorkTimeService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wardziniak on 1/2/15.
 */
@Module(
        injects = { App.class, WorkTimeService.class },
        library = true
)
public class AppModule {

    private App app;

    private Bus eventBus;

    private Ringtone ringtone;

    public AppModule(App app) {
        this.app = app;
        this.eventBus = new MainThreadBus();
        this.ringtone = RingtoneManager.getRingtone(app, Settings.System.DEFAULT_ALARM_ALERT_URI);
    }

    @Provides
    public Application getApplication() {
        return  app;
    }

    @Provides
    @Singleton
    public Ringtone getRingtone() {
        return ringtone;
    }

    @Singleton
    @Provides
    public Bus getEventBus() {
        return eventBus;
    }
}
