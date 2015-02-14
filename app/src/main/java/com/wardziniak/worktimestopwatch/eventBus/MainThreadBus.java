package com.wardziniak.worktimestopwatch.eventBus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by wardziniak on 1/4/15.
 */
public class MainThreadBus extends Bus {

    public MainThreadBus() {
        super();
        //Log.d("MainThreadBus", "constructos");
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
