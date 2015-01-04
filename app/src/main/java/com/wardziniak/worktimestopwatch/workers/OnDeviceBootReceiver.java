package com.wardziniak.worktimestopwatch.workers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnDeviceBootReceiver extends BroadcastReceiver {
    public OnDeviceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // When device boots, we should remove any pending alarms
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
