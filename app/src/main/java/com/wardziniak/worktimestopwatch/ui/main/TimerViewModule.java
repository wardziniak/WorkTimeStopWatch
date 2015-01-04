package com.wardziniak.worktimestopwatch.ui.main;

import com.wardziniak.worktimestopwatch.AppModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wardziniak on 1/3/15.
 */
@Module(
        injects = TimerViewFragment.class,
        addsTo = AppModule.class
)
public class TimerViewModule {

    private TimerView timerView;

    public TimerViewModule(TimerView timerView) {
        this.timerView = timerView;
    }

    @Provides
    public TimerViewPresenter getTimerViewPresenter() {
        return new TimerViewPresenterImpl(timerView);
    }
}
