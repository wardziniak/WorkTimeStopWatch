package com.wardziniak.worktimestopwatch.ui.common;

import android.content.res.Resources;

/**
 * Created by wardziniak on 1/2/15.
 */
public interface MainView {

    public void switchToTimerView(int selectedItem);

    public void switchToTimerSettingsView(int selectedItem);

    public void switchToHistoryView(int selectedItem);

    public Resources getResources();
}
