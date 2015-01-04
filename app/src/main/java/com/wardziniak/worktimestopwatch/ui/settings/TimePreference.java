package com.wardziniak.worktimestopwatch.ui.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.wardziniak.worktimestopwatch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wardziniak on 1/2/15.
 */
public class TimePreference extends DialogPreference {

    private final static int DEFAULT_HOURS = 8;
    private final static int DEFAULT_MINUTES = 0;

    private Calendar calendar;

    private TimePicker picker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(R.string.dialogSetLabel);
        setNegativeButtonText(R.string.dialogCancelLabel);
        calendar = Calendar.getInstance();
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(true);
        return picker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
            Log.d("onDialogClosed", ":" + picker.getCurrentMinute() +":" + picker.getCurrentHour());
            Log.d("onDialogClosed", ":" + (60 * 1000 * (picker.getCurrentMinute() + 60 * picker.getCurrentHour())));
            persistLong(60 * 1000 * (picker.getCurrentMinute() + 60 * picker.getCurrentHour()));
            setSummary(getSummary());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final Long defaultLong;
        if (defaultValue == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, DEFAULT_HOURS);
            cal.set(Calendar.MINUTE, DEFAULT_MINUTES);
            defaultLong = cal.getTimeInMillis();
        }
        else
            defaultLong = Long.parseLong((String) defaultValue);
        if (restorePersistedValue) {
            final long dayTime = getPersistedLong(defaultLong);
            final int minutes = (int) (dayTime / (60 * 1000)) % 60;
            final int hours = (int) (dayTime - minutes*60*1000) / (60*1000*60);
            Log.d("onSetInitialValue", ":" + hours + ":" + minutes + ":" + dayTime);
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            //calendar.set(Calendar.HOUR_OF_DAY, (int) dayTime / (60 * 60 * 1000));
            //calendar.set(Calendar.MINUTE, (int) (dayTime / (60 * 1000)) % 60);
            //calendar.setTimeInMillis(getPersistedLong(defaultLong));
        }
        else
            calendar.setTimeInMillis(defaultLong);
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        if (calendar == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(calendar.getTime());
    }
}
