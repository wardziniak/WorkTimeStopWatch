package com.wardziniak.worktimestopwatch.ui.common;

import com.wardziniak.worktimestopwatch.ui.main.TimerViewPresenter;

/**
 * Created by wardziniak on 1/2/15.
 */
public interface MainPresenter {

    public void onDrawerClick(int position);

    public String[] getDrawerTitles();

    public TimerViewPresenter getTimerViewPresenter();
}
