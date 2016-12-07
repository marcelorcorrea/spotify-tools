package com.marcelorcorrea.spotifytools.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.marcelorcorrea.spotifytools.R;
import com.marcelorcorrea.spotifytools.services.SpotifyIntentService;
import com.marcelorcorrea.spotifytools.utils.PreferencesManager;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = MyReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isFeatureEnable = PreferencesManager.getInstance().getBooleanPreference(context.getString(R.string.enable_key), false);
        if (isFeatureEnable) {
            String action = intent.getAction();
            String message;
            String deviceName = PreferencesManager.getInstance().getStringPreference(context.getString(R.string.bluetooth_device_name_key));
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (deviceName.equals(device.getName())) {
                        message = "Bluetooth Connected!\n" + device.getName() + " - " + device.getAddress();
                        SpotifyIntentService.startActionSpotify(context);
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    message = "Bluetooth Disconnected!";
                    SpotifyIntentService.startActionDisconnected(context);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    break;
                case SpotifyIntentService.SPOTIFY_PLAYBACKSTATE_CHANGED:
                    Log.d(TAG, action);
                    Log.d(TAG, bundle2string(intent.getExtras()));
                    Log.d(TAG, "*********************************");
                    SpotifyIntentService.startActionStateChanged(context, intent.getExtras());
            }
        }
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
