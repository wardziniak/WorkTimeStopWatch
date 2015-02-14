package com.wardziniak.worktimestopwatch.workers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

public class WorkStateChangeReceiver extends BroadcastReceiver {

    public static final String ACTION_WORK_HAS_FINISHED_ALARM = "com.wardziniak.worktimestopwatch.finish_work";

    public WorkStateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            if (shouldStartWork(context, intent))
                workHasStarted(context);

/*
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                final int networkId = SharedPreferenceHelper.getNetworkId(context);
                if (networkId == wifiInfo.getNetworkId()) {
                    // Connected to chosen wifi, set timer
                    //setWorkAlarm(context);
                    workHasStarted(context);
                }
            }*/
        }
        else if (WorkTimeService.ACTION_WORK_TIME_ELAPSED.equals(intent.getAction())) {
            workTimeHasElapsed(context);
        }
    }

    private boolean shouldStartWork(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            final int networkId = SharedPreferenceHelper.getNetworkId(context);
            final boolean isWorking = SharedPreferenceHelper.isWorking(context);
            return !isWorking && networkId == wifiInfo.getNetworkId();
        }
        return false;
    }

    private void workHasStarted(Context context) {
        startWorkTimeService(context, WorkTimeService.ACTION_WORK_HAS_STARTED);
    }

    private void workTimeHasElapsed(Context context) {
        startWorkTimeService(context, WorkTimeService.ACTION_WORK_TIME_ELAPSED);
    }

    public void startWorkTimeService(Context context, String action) {
        Intent intent = new Intent(context, WorkTimeService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    private void setWorkAlarm(Context context) {
        final long workStartTime = SystemClock.elapsedRealtime();
        final long triggerTime = workStartTime + SharedPreferenceHelper.getWorkTime(context);
        Log.d("WorkStateChangeReceiver", ":" + workStartTime + ":" + triggerTime);
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, WorkHasFinishedReceiver.class);
        i.setAction(WorkHasFinishedReceiver.ACTION_WORK_HAS_FINISHED_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP ,  triggerTime, pendingIntent);
        SharedPreferenceHelper.setWorkFinishTime(context, triggerTime);
        SharedPreferenceHelper.setWorkStartTime(context, workStartTime);
        context.startService(new Intent(context, WorkTimeService.class));
        Log.d("ConnectedToWifiReceiver", "ustawiono alarm");
    }
}
