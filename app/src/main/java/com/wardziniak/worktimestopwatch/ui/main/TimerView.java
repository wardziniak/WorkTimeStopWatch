package com.wardziniak.worktimestopwatch.ui.main;

import android.content.Context;

/**
 * Created by wardziniak on 1/3/15.
 */
public interface TimerView {

    public Context getContext();

    public void setTimerMinTime(long min);

    public void setTimerMaxTime(long max);

    public void setTimerCurrentTime(long current);

    public void startTimer();

    public void stopTimer();

    public void showTimePicker(int minHours, int minMinutes);

}
