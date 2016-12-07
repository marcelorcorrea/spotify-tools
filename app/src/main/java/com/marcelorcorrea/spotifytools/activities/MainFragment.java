package com.marcelorcorrea.spotifytools.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.marcelorcorrea.spotifytools.R;
import com.marcelorcorrea.spotifytools.receivers.MyReceiver;
import com.marcelorcorrea.spotifytools.services.SpotifyIntentService;
import com.marcelorcorrea.spotifytools.utils.PreferencesManager;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getName();

    private MyReceiver receiver;

    public MainFragment() {
        receiver = new MyReceiver();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ToggleButton toggle = (ToggleButton) root.findViewById(R.id.togglebutton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isFeatureEnable = PreferencesManager.getInstance().getBooleanPreference(getString(R.string.enable_key), false);
                if (isFeatureEnable) {
                    if (isChecked) {
                        SpotifyIntentService.startActionSpotify(getContext());
                    } else {
                        SpotifyIntentService.startActionStopSong(getContext());
                    }
                } else {
                    Toast.makeText(getContext(), "Please enable the feature in settings first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
