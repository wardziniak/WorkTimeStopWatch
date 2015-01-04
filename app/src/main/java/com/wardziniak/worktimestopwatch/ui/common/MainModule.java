package com.wardziniak.worktimestopwatch.ui.common;

import com.wardziniak.worktimestopwatch.AppModule;
import com.wardziniak.worktimestopwatch.ui.history.WorkHistoryFragment;
import com.wardziniak.worktimestopwatch.ui.main.TimerViewFragment;
import com.wardziniak.worktimestopwatch.ui.settings.TimerSettingsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wardziniak on 1/2/15.
 */
@Module(
       injects = MainActivity.class,
        addsTo = AppModule.class
)
public class MainModule {

    private MainView mainView;

    public MainModule(MainView mainView) {
        this.mainView = mainView;
    }

    @Singleton
    @Provides
    public MainPresenter getMainPresenter() {
        return new MainPresenterImpl(mainView);
    }

    @Singleton
    @Provides
    public TimerViewFragment getTimerViewFragment() {
        return TimerViewFragment.newInstance();
    }

    @Singleton
    @Provides
    public WorkHistoryFragment getWorkHisoryFragment() {
        return WorkHistoryFragment.newInstance();
    }

    @Singleton
    @Provides
    public TimerSettingsFragment getTimerSettingsFragment() {
        return TimerSettingsFragment.newInstance();
    }
}
