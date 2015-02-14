package com.wardziniak.worktimestopwatch.ui.main;


import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.wardziniak.worktimestopwatch.App;
import com.wardziniak.worktimestopwatch.R;
import com.wardziniak.worktimestopwatch.ui.widgets.TimeCounterView;
import com.wardziniak.worktimestopwatch.workers.model.OnWorkStartSignal;
import com.wardziniak.worktimestopwatch.workers.model.WorkHasFinished;
import com.wardziniak.worktimestopwatch.workers.model.WorkTimeHasChangeSignal;
import com.wardziniak.worktimestopwatch.workers.model.WorkWasCanceled;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Inject
    Bus eventBus;

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
        ButterKnife.inject(this, view);
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
        eventBus.register(this);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new TimerViewModule(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        activityGraph = null;
    }

    @Override
    public void showTimePicker(final int minHours, final int minMinutes) {
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timerViewPresenter.extendWork(hourOfDay, minute);
            }
        }, minHours, minMinutes, true) {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < minHours)
                    view.setCurrentHour(minHours);
                if (minute < minMinutes)
                    view.setCurrentMinute(minMinutes);
            }
        };
        timePicker.show();
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

    @Subscribe
    public void onWorkTimeChange(WorkTimeHasChangeSignal workTimeHasChangeSignal) {
        Log.d("TimerViewFragment:" , "onWorkStart");
        timerViewPresenter.workTimeChange();
    }

    @Subscribe
    public void onWorkHasBeenFinished(WorkHasFinished workHasFinished) {
        timerViewPresenter.onWorkTimeEnd();
    }

    @Subscribe
    public void onWorkWasCanceled(WorkWasCanceled workWasCanceled) {
        timerViewPresenter.onWorkTimeEnd();
    }

    @OnClick(R.id.btnCancelWork)
    public void cancelWork(View view) {
        Log.d("TimerViewFragment", "cancelWork");
        timerViewPresenter.cancelWork();
    }

    @OnClick(R.id.btnExtendWork)
    public void extendWork(View view) {
        Log.d("TimerViewFragment", "extendWork");
        timerViewPresenter.prepareToExtend();
    }

    @OnClick(R.id.btnFinishWork)
    public void finishWork(View view) {
        Log.d("TimerViewFragment", "finishWork");
        timerViewPresenter.finishWork();
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
