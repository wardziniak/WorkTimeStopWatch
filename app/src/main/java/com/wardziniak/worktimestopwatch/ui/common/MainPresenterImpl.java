package com.wardziniak.worktimestopwatch.ui.common;

import android.app.Fragment;
import android.util.Log;

import com.wardziniak.worktimestopwatch.R;
import com.wardziniak.worktimestopwatch.ui.main.TimerViewPresenter;
import com.wardziniak.worktimestopwatch.ui.main.TimerViewPresenterImpl;

/**
 * Created by wardziniak on 1/2/15.
 */
public class MainPresenterImpl implements MainPresenter {

    public final static int TIMER_VIEW_POSITION = 0;
    public final static int HISTORY_VIEW_POSITION = 1;
    public final static int SETTINGS_VIEW_POSITION = 2;

    private MainView mainView;

    private int selectedItem = 0;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }


    @Override
    public void switchToFragment(int fragmentIndex) {
        this.selectedItem = fragmentIndex;
        mainView.switchToTimerView(selectedItem);
    }

    @Override
    public int getIndexOfDefaultFragment() {
        return selectedItem;
    }

    @Override
    public void onDrawerClick(int position) {
        this.selectedItem = position;
        switch (position) {
            case TIMER_VIEW_POSITION:
                mainView.switchToTimerView(selectedItem);
                break;
            case HISTORY_VIEW_POSITION:
                mainView.switchToHistoryView(selectedItem);
                break;
            case SETTINGS_VIEW_POSITION:
                mainView.switchToTimerSettingsView(selectedItem);
                break;
        }
        Log.d("MainPresenterImpl", "onDrawerClick:" + position);
    }

    @Override
    public String[] getDrawerTitles() {
        return mainView.getResources().getStringArray(R.array.fragments_array);
    }

    @Override
    public TimerViewPresenter getTimerViewPresenter() {
        return null;
    }
}
