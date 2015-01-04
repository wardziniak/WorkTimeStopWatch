package com.wardziniak.worktimestopwatch;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by wardziniak on 1/2/15.
 */
public class App extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this));
    }
}
