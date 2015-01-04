package com.wardziniak.worktimestopwatch.ui.main;

import android.os.SystemClock;
import android.util.Log;

import com.wardziniak.worktimestopwatch.workers.SharedPreferenceHelper;

/**
 * Created by wardziniak on 1/3/15.
 */
public class TimerViewPresenterImpl implements TimerViewPresenter {

    private TimerView timerView;

    public TimerViewPresenterImpl(TimerView timerView) {
        this.timerView = timerView;
    }

    @Override
    public void onStartView() {
        final long workFinishTime = SharedPreferenceHelper.getWorkFinishTime(timerView.getContext());
        final long workStartTime = SharedPreferenceHelper.getWorkStartTime(timerView.getContext());
        final long currentTime = SystemClock.elapsedRealtime();
        Log.d("TimerViewPresenterImpl", "workStartTime: " + workStartTime + ":workFinishTime:" + workFinishTime
            + ":currentTime:" + currentTime);
        timerView.setTimerCurrentTime(currentTime);
        timerView.setTimerMinTime(workStartTime);
        timerView.setTimerMaxTime(workFinishTime);
        if (currentTime < workFinishTime)
            timerView.startTimer();
        else
            timerView.stopTimer();
    }

    @Override
    public void extendWork() {

    }

    @Override
    public void finishWork() {

    }

    @Override
    public void cancelWork() {

    }
}
