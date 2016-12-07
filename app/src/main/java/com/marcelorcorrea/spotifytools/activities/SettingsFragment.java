package com.marcelorcorrea.spotifytools.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.marcelorcorrea.spotifytools.R;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_generals);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.bluetooth_device_name_key)));
    }

    private static Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);

        preferenceChangeListener.onPreferenceChange(preference,
                android.preference.PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.enable_key))) {
            boolean enabled = sharedPreferences.getBoolean(key, true);
            findPreference(getString(R.string.bluetooth_device_name_key)).setEnabled(enabled);
            findPreference(getString(R.string.open_spotify_key)).setEnabled(enabled);
        }
    }
}
