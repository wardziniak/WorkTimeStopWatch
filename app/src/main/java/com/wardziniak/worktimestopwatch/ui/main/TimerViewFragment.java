package com.wardziniak.worktimestopwatch.ui.main;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wardziniak.worktimestopwatch.App;
import com.wardziniak.worktimestopwatch.R;
import com.wardziniak.worktimestopwatch.ui.common.MainModule;
import com.wardziniak.worktimestopwatch.ui.widgets.TimeCounterView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerViewFragment extends Fragment implements TimerView {

    //private Chronometer chronometer;

    private TimeCounterView timeCounterViewToFinish;
    private TimeCounterView timeCounterViewFromStart;

    @Inject
    TimerViewPresenter timerViewPresenter;

    private ObjectGraph activityGraph;

    public static TimerViewFragment newInstance() {
        return new TimerViewFragment();
    }

    public TimerViewFragment() {
        // Required empty public constructor
        Log.d("TimerViewFragment", "constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer_view, container, false);
        timeCounterViewToFinish = (TimeCounterView) view.findViewById(R.id.timeCounterToFinish);
        timeCounterViewFromStart = (TimeCounterView) view.findViewById(R.id.timeCounterFromStart);
        return view;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        timerViewPresenter.onStartView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(getModules().toArray());
        activityGraph.inject(this);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new TimerViewModule(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    @Override
    public void setTimerMinTime(long min) {
        timeCounterViewFromStart.setMin(min);
        timeCounterViewFromStart.setMin(min);
    }

    @Override
    public void setTimerMaxTime(long max) {
        timeCounterViewFromStart.setMax(max);
        timeCounterViewToFinish.setMax(max);
    }

    @Override
    public void setTimerCurrentTime(long current) {
        timeCounterViewFromStart.setCurrentTime(current);
        timeCounterViewToFinish.setCurrentTime(current);
    }

    @Override
    public void startTimer() {
        timeCounterViewToFinish.start();
        timeCounterViewFromStart.start();
    }

    @Override
    public void stopTimer() {
        timeCounterViewFromStart.stop();
        timeCounterViewToFinish.stop();
    }

    /*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //chronometer.setBase(30000);
        //chronometer.setBase(SharedPreferenceHelper.getWorkTime(getActivity()));
        Log.d("TimerViewFragment", "onActivityCreated");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TimerViewFragment", "onCreate");
    }*/
}
