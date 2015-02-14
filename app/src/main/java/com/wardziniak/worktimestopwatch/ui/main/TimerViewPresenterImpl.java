package com.wardziniak.worktimestopwatch.ui.main;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.wardziniak.worktimestopwatch.workers.SharedPreferenceHelper;
import com.wardziniak.worktimestopwatch.workers.WorkTimeService;

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
        if (currentTime <= workFinishTime)
            timerView.startTimer();
        else
            timerView.stopTimer();
    }

    @Override
    public void workTimeChange() {
        onStartView();
    }

    @Override
    public void onWorkTimeEnd() {
        //onStartView();
    }

    @Override
    public void prepareToExtend() {
        final long workFinishTime = SharedPreferenceHelper.getWorkFinishTime(timerView.getContext());
        final long workStartTime = SharedPreferenceHelper.getWorkStartTime(timerView.getContext());
        final long workTime = workFinishTime - workStartTime;
        final int minutes = (int) (workTime / (60 * 1000)) % 60;
        final int hours = (int) (workTime - minutes*60*1000) / (60*1000*60);
        timerView.showTimePicker(hours, minutes);
    }

    @Override
    public void extendWork(int hours, int minutes) {
        Intent intent = new Intent(timerView.getContext(), WorkTimeService.class);
        intent.setAction(WorkTimeService.ACTION_TIME_WORK_EXTENDED);
        long triggerTime = 60 * 1000 *(minutes + 60 * hours);
        intent.putExtra(WorkTimeService.EXTENDED_TIME_PARAM, triggerTime);
        timerView.getContext().startService(intent);
    }

    @Override
    public void finishWork() {
        Intent intent = new Intent(timerView.getContext(), WorkTimeService.class);
        intent.setAction(WorkTimeService.ACTION_WORK_HAS_FINISHED);
        timerView.getContext().startService(intent);
    }

    @Override
    public void cancelWork() {
        Intent intent = new Intent(timerView.getContext(), WorkTimeService.class);
        intent.setAction(WorkTimeService.ACTION_WORK_HAS_BEEN_CANCELED);
        timerView.getContext().startService(intent);
    }

}
