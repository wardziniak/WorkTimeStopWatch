package com.wardziniak.worktimestopwatch.workers;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.otto.Bus;
import com.wardziniak.worktimestopwatch.App;
import com.wardziniak.worktimestopwatch.R;
import com.wardziniak.worktimestopwatch.ui.common.MainActivity;
import com.wardziniak.worktimestopwatch.ui.common.MainPresenterImpl;
import com.wardziniak.worktimestopwatch.ui.widgets.TimeCounterView;
import com.wardziniak.worktimestopwatch.workers.model.OnWorkStartSignal;
import com.wardziniak.worktimestopwatch.workers.model.WorkHasFinished;
import com.wardziniak.worktimestopwatch.workers.model.WorkTimeHasChangeSignal;
import com.wardziniak.worktimestopwatch.workers.model.WorkWasCanceled;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WorkTimeService extends IntentService {

    public static final String ACTION_WORK_HAS_STARTED = "com.wardziniak.worktimestopwatch.action.work_has_started";
    public static final String ACTION_WORK_TIME_ELAPSED = "com.wardziniak.worktimestopwatch.action.work_time_elapsed";
    public static final String ACTION_WORK_HAS_FINISHED = "com.wardziniak.worktimestopwatch.action.work_has_finished";
    public static final String ACTION_TIME_WORK_EXTENDED = "com.wardziniak.worktimestopwatch.action.time_work_extended";
    public static final String ACTION_WORK_HAS_BEEN_CANCELED = "com.wardziniak.worktimestopwatch.action.work_has_been_canceled";
    public static final String ACTION_TEST = "com.wardziniak.worktimestopwatch.action.test";


    public static final String EXTENDED_TIME_PARAM = "com.wardziniak.worktimestopwatch.extra.EXTENDED_TIME";
    public static final String PARAM_IS_WORK_TIME_ELAPSED = "com.wardziniak.worktimestopwatch.extra.IS_WORK_TIME_ELAPSED";
    public static final String PARAM_FRAGMENT_INDEX = "com.wardziniak.worktimestopwatch.extra.FRAGMENT_INDEX";

    private ObjectGraph objectGraph;

    @Inject
    Bus eventBus;

    @Inject
    Ringtone ringtone;

    public WorkTimeService() {
        super("WorkTimeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //objectGraph = ((App) getApplication()).createScopedGraph(getModules().toArray());
        objectGraph = ((App) getApplication()).getObjectGraph();
        objectGraph.inject(this);
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        objectGraph = null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_WORK_HAS_STARTED.equals(intent.getAction())) {
            startWork();
        }
        else if (ACTION_WORK_HAS_FINISHED.equals(intent.getAction())) {
            finishWork();
        }
        else if (ACTION_WORK_HAS_BEEN_CANCELED.equals(intent.getAction())) {
            cancelWork();
        }
        else if (ACTION_TIME_WORK_EXTENDED.equals(intent.getAction())) {
            extendWorkTime(intent.getLongExtra(EXTENDED_TIME_PARAM, -1l));
        }
        else if (ACTION_WORK_TIME_ELAPSED.equals(intent.getAction())) {
            timeElapsed();
        }
        Log.d("WorkTimeService", "onHandleIntent:" + intent.getAction());
    }

    private void startWork() {
        Log.d("WorkTimeService", "startWork:");
        setAlarm(false, SharedPreferenceHelper.getWorkTime(getApplicationContext()));
        //SharedPreferenceHelper.setWorking(getApplicationContext(), true);
        //modifyWorkTime(SystemClock.elapsedRealtime(), -1l);
        updateNotification(false);
/*        final long workStartTime = SystemClock.elapsedRealtime();
        final long triggerTime = workStartTime + SharedPreferenceHelper.getWorkTime(getApplicationContext());
        final AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), WorkStateChangeReceiver.class);
        i.setAction(WorkTimeService.ACTION_WORK_TIME_ELAPSED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        SharedPreferenceHelper.setWorkFinishTime(getApplicationContext(), triggerTime);
        SharedPreferenceHelper.setWorkStartTime(getApplicationContext(), workStartTime);
        Log.d("WorkTimeService", "startWork");
        eventBus.post(new WorkTimeHasChangeSignal());*/
    }

    private void finishWork() {
        Log.d("WorkTimeService", "finishWork");
        removeAlarm();
        eventBus.post(new WorkHasFinished());
    }

    private void cancelWork() {
        Log.d("WorkTimeService", "cancelWork");
        removeAlarm();
        eventBus.post(new WorkWasCanceled());
    }

    private void extendWorkTime(long newWorkTime) {
        Log.d("WorkTimeService", "extendWorkTime:" + newWorkTime);
        setAlarm(true, newWorkTime);
        //modifyWorkTime(-1, newWorkTime);
        updateNotification(true);
        //eventBus.post(new WorkTimeHasChangeSignal());
    }

    private void timeElapsed() {
        Log.d("WorkTimeService", "timeElapsed");
        updateNotification(true);
        //alarmNotification();
    }

    private RemoteViews prepareRemoteViews(boolean isTimeElapsed) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setChronometer(R.id.timeCounterFromStart, SharedPreferenceHelper.getWorkStartTime(getApplication()), "%s", true);
        //remoteViews.setO

/*        Intent notificationIntent = new Intent(getApplicationContext(), WorkTimeService.class);
        notificationIntent.setAction(WorkTimeService.ACTION_TEST);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.textViewStartService, pendingIntent);

        notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra(WorkTimeService.PARAM_IS_WORK_TIME_ELAPSED, isTimeElapsed);
        notificationIntent.putExtra(WorkTimeService.PARAM_FRAGMENT_INDEX, MainPresenterImpl.TIMER_VIEW_POSITION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        remoteViews.setOnClickPendingIntent(R.id.textViewStartActivity, pendingIntent);*/

        return remoteViews;
    }


    // When time ellapsed we should change notification. Add alarm and sound.
    // Action should moved to TimerFragemtn in Activity
    public void updateNotification(boolean isTimeElapsed) {
        //RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon_notification_medal)
                .setContent(prepareRemoteViews(isTimeElapsed));
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(WorkTimeService.PARAM_IS_WORK_TIME_ELAPSED, isTimeElapsed);
        resultIntent.putExtra(WorkTimeService.PARAM_FRAGMENT_INDEX, MainPresenterImpl.TIMER_VIEW_POSITION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);


        //remoteViews.setChronometer(R.id.timeCounterFromStart, SharedPreferenceHelper.getWorkStartTime(getApplication()), "%s", true);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        if (isTimeElapsed && !ringtone.isPlaying())
            ringtone.play();
        else if (!isTimeElapsed && ringtone.isPlaying())
            ringtone.stop();
    }

    public void alarmNotification() {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon_notification_medal)
                .setAutoCancel(true)
                .setContent(remoteViews);
        remoteViews.setChronometer(R.id.timeCounterFromStart, SharedPreferenceHelper.getWorkStartTime(getApplication()), "%s", true);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        //Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);
        ringtone.play();
    }

    private void removeAlarm() {
        final AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), WorkStateChangeReceiver.class);
        i.setAction(WorkTimeService.ACTION_WORK_TIME_ELAPSED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
        SharedPreferenceHelper.setWorkFinishTime(getApplicationContext(), SharedPreferenceHelper.ALARM_WAS_NOT_SET);
        SharedPreferenceHelper.setWorkStartTime(getApplicationContext(), SharedPreferenceHelper.ALARM_WAS_NOT_SET);
        SharedPreferenceHelper.setWorking(getApplicationContext(), false);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        //Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);
        if (ringtone.isPlaying())
            ringtone.stop();
        SharedPreferenceHelper.setWorking(getApplicationContext(), false);
    }

    private void setAlarm(boolean isExtending, long workTime) {
        final long workStartTime = isExtending ? SharedPreferenceHelper.getWorkStartTime(getApplicationContext()) : SystemClock.elapsedRealtime();
        final long triggerTime = workStartTime + workTime;
        final AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), WorkStateChangeReceiver.class);
        i.setAction(WorkTimeService.ACTION_WORK_TIME_ELAPSED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        SharedPreferenceHelper.setWorking(getApplicationContext(), true);
        SharedPreferenceHelper.setWorkFinishTime(getApplicationContext(), triggerTime);
        if (!isExtending)
            SharedPreferenceHelper.setWorkStartTime(getApplicationContext(), workStartTime);
        Log.d("WorkTimeService", "modifyWorkTime");
        eventBus.post(new WorkTimeHasChangeSignal());
    }

    private void modifyWorkTime(long startTime, long newWorkTime) {
        final long workStartTime = startTime == -1 ? SharedPreferenceHelper.getWorkStartTime(getApplicationContext()) : SystemClock.elapsedRealtime();
        long workTime = newWorkTime != -1 ? newWorkTime :SharedPreferenceHelper.getWorkTime(getApplicationContext());
        final long triggerTime = workStartTime + workTime;
        final AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), WorkStateChangeReceiver.class);
        i.setAction(WorkTimeService.ACTION_WORK_TIME_ELAPSED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        SharedPreferenceHelper.setWorkFinishTime(getApplicationContext(), triggerTime);
        SharedPreferenceHelper.setWorkStartTime(getApplicationContext(), workStartTime);
        SharedPreferenceHelper.setWorking(getApplicationContext(), true);
        Log.d("WorkTimeService", "modifyWorkTime");
        eventBus.post(new WorkTimeHasChangeSignal());
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList();
    }

}
