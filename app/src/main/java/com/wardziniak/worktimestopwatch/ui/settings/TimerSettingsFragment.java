package com.wardziniak.worktimestopwatch.ui.settings;


import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;

import com.wardziniak.worktimestopwatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerSettingsFragment extends PreferenceFragment {

    public static TimerSettingsFragment newInstance() {
        return new TimerSettingsFragment();
    }

    public TimerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }



}
