package com.marcelorcorrea.spotifytools;

import android.app.Application;

import com.marcelorcorrea.spotifytools.utils.PreferencesManager;


public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);
    }

    private boolean isRunningForTheFirstTime = true;

    public boolean isRunningForTheFirstTime() {
        return isRunningForTheFirstTime;
    }

    public void setRunningForTheFirstTime(boolean runningForTheFirstTime) {
        isRunningForTheFirstTime = runningForTheFirstTime;
    }
}
