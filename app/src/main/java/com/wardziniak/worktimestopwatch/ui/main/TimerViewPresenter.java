package com.wardziniak.worktimestopwatch.ui.main;

/**
 * Created by wardziniak on 1/3/15.
 */
public interface TimerViewPresenter {

    public void onStartView();

    public void extendWork(int hours, int minutes);

    public void prepareToExtend();

    public void finishWork();

    public void cancelWork();

    public void workTimeChange();

    public void onWorkTimeEnd();

}
