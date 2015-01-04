package com.wardziniak.worktimestopwatch.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by wardziniak on 1/2/15.
 */
public class SharedPreferenceHelper {

    public final static String WIFI_NETWORK_ID_PREF_KEY = "wifi_list_key";

    public final static String WORK_TIME_KEY = "work_time_key";

    public final static String WORK_FINISH_TIME_KEY = "work_finish_time_key";

    public final static String WORK_START_TIME_KEY = "work_start_time_key";

    public final static String IS_WORKING_KEY = "is_working_key";

    public final static long DEFAULT_WORK_TIME = 8 * 60 * 60 * 1000;

    public final static long ALARM_WAS_NOT_SET = -1;

    public static int getNetworkId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPreferences.getString(WIFI_NETWORK_ID_PREF_KEY, "-1"));
    }

    public static long getWorkTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(WORK_TIME_KEY, DEFAULT_WORK_TIME);
    }

    public static long getWorkFinishTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(WORK_FINISH_TIME_KEY, ALARM_WAS_NOT_SET);
    }

    public static void setWorkFinishTime(Context context, long workFinishTime) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(WORK_FINISH_TIME_KEY, workFinishTime).commit();
    }

    public static void setWorkStartTime(Context context, long workStartTime) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(WORK_START_TIME_KEY, workStartTime).commit();
    }

    public static long getWorkStartTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(WORK_START_TIME_KEY, ALARM_WAS_NOT_SET);
    }

}
