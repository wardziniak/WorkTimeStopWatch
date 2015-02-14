package com.wardziniak.worktimestopwatch.workers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.wardziniak.worktimestopwatch.R;

public class WorkHasFinishedReceiver extends BroadcastReceiver {

    public static final String ACTION_WORK_HAS_FINISHED_ALARM = "com.wardziniak.worktimestopwatch.finish_work";

    public WorkHasFinishedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WorkHasFinishedReceiver", "Time has elapsed");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);

        //remoteViews.setTextViewText(R.id.textView1, "To jest alarm");
        //remoteViews.setTextViewText(R.id.textView1, "koniec pracy");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_notification_medal)
                .setAutoCancel(true)
                .setContent(remoteViews);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
}
