package com.marcelorcorrea.spotifytools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PreferencesManager {


    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public String getStringPreference(String key) {
        return mPref.getString(key, "");
    }

    public boolean setStringPreference(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public float getFloatPreference(Context context, String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public boolean setFloatPreference(Context context, String key, float value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public long getLongPreference(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public boolean setLongPreference(String key, long value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public int getIntegerPreference(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public boolean setIntegerPreference(String key, int value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean getBooleanPreference(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public boolean setBooleanPreference(String key, boolean value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }
}