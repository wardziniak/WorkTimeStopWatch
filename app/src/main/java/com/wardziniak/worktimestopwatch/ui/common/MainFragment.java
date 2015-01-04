package com.wardziniak.worktimestopwatch.ui.common;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by wardziniak on 1/3/15.
 */
public abstract class MainFragment extends Fragment {

    private boolean mFirstAttach = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
