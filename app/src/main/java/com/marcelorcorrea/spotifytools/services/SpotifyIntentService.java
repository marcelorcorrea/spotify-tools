package com.marcelorcorrea.spotifytools.services;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.marcelorcorrea.spotifytools.Global;
import com.marcelorcorrea.spotifytools.R;
import com.marcelorcorrea.spotifytools.utils.PreferencesManager;

import java.util.List;


public class SpotifyIntentService extends IntentService {
    private static final String TAG = SpotifyIntentService.class.getName();

    private static final String ACTION_OPEN_SPOTIFY = "marcelorcorrea.com.spotifytools.services.action.OPEN_SPOTIFY";
    private static final String ACTION_STATE_CHANGED = "marcelorcorrea.com.spotifytools.services.action.STATE_CHANGED";
    private static final String ACTION_STOP_SONG = "marcelorcorrea.com.spotifytools.services.action.STOP_SONG";
    private static final String ACTION_DISCONNECTED = "marcelorcorrea.com.spotifytools.services.action.DISCONNECTED";
    /**
     * Spotify Constants
     */
    public static final String SPOTIFY_PLAYBACKSTATE_CHANGED = "com.spotify.music.playbackstatechanged";
    public static final String SPOTIFY_QUEUE_CHANGED = "com.spotify.music.queuechanged";
    public static final String SPOTIFY_METADATA_CHANGED = "com.spotify.music.metadatachanged";
    public static final String SPOTIFY_PLAYING_FIELD = "playing";
    public static final String SPOTIFY_DURATION_FIELD = "duration";
    public static final String SPOTIFY_LENGTH_FIELD = "length";
    public static final String SPOTIFY_ARTIST_FIELD = "artist";
    public static final String SPOTIFY_ALBUM_ID_FIELD = "albumId";

    private static final String SPOTIFY_PACKAGE = "com.spotify.music";
    private static final String SPOTIFY_MAIN_ACTIVITY = "com.spotify.music.MainActivity";
    private static final String SPOTIFY_MEDIA_BUTTON_RECEIVER = "com.spotify.music.internal.receiver.MediaButtonReceiver";
    private final ComponentName spotifyMediaButtonComponent = new ComponentName(SPOTIFY_PACKAGE, SPOTIFY_MEDIA_BUTTON_RECEIVER);
    private final ComponentName spotifyMainActivityComponent = new ComponentName(SPOTIFY_PACKAGE, SPOTIFY_MAIN_ACTIVITY);
    /**
     * Spotify Constants End
     */
    private static final String SPOTIFY_NOT_INSTALLED = "Spotify is not installed on this device.";
    private Handler mMainThreadHandler = null;

    public SpotifyIntentService() {
        super("SpotifyIntentService");
        mMainThreadHandler = new Handler();
    }

    public static void startActionSpotify(Context context) {
        Intent intent = new Intent(context, SpotifyIntentService.class);
        intent.setAction(ACTION_OPEN_SPOTIFY);
        context.startService(intent);
    }

    public static void startActionStopSong(Context context) {
        Intent intent = new Intent(context, SpotifyIntentService.class);
        intent.setAction(ACTION_STOP_SONG);
        context.startService(intent);
    }

    public static void startActionStateChanged(Context context, Bundle extras) {
        Intent intent = new Intent(context, SpotifyIntentService.class);
        intent.setAction(ACTION_STATE_CHANGED);
        intent.putExtras(extras);
        context.startService(intent);
    }

    public static void startActionDisconnected(Context context) {
        Intent intent = new Intent(context, SpotifyIntentService.class);
        intent.setAction(ACTION_DISCONNECTED);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_OPEN_SPOTIFY:
                    if (isPackageInstalled()) {
                        boolean openSpotify = PreferencesManager.getInstance().getBooleanPreference(getString(R.string.open_spotify_key), false);
                        if (openSpotify) {
                            openSpotify();
                        }
                        stopSong();
                        playSong();
                    } else {
                        mMainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SpotifyIntentService.this, SPOTIFY_NOT_INSTALLED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case ACTION_STATE_CHANGED:
                    handleStateChanged(intent.getExtras());
                    break;
                case ACTION_STOP_SONG:
                    stopSong();
                    break;
                case ACTION_DISCONNECTED:
                    disconnect();
                    break;
            }
        }
    }

    private void disconnect() {
        ((Global) getApplication()).setRunningForTheFirstTime(true);
    }

    private void handleStateChanged(Bundle extras) {
        boolean playing = extras.getBoolean(SPOTIFY_PLAYING_FIELD);
        long duration = extras.getLong(SPOTIFY_DURATION_FIELD);
        int length = extras.getInt(SPOTIFY_LENGTH_FIELD);
        String artist = extras.getString(SPOTIFY_ARTIST_FIELD, "");
        String albumId = extras.getString(SPOTIFY_ALBUM_ID_FIELD, "");
        boolean firstTime = ((Global) getApplication()).isRunningForTheFirstTime();
        if (!artist.isEmpty() && !albumId.isEmpty() && duration != 0 && length != 0 && !playing && firstTime) {
            Log.d(TAG, "Not playing and it is the first time!");
            stopSong();
            playSong();
            ((Global) getApplication()).setRunningForTheFirstTime(false);
        } else if (playing && firstTime) {
            ((Global) getApplication()).setRunningForTheFirstTime(false);
        }
    }

    public void openSpotify() {
        Intent launcher = new Intent(Intent.ACTION_VIEW);
        launcher.setComponent(spotifyMainActivityComponent);
        launcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launcher);
    }

    public void playSong() {
        Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        i.setComponent(spotifyMediaButtonComponent);
        i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY));
        sendBroadcast(i);
    }

    public void stopSong() {
        Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        i.setComponent(spotifyMediaButtonComponent);
        i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_STOP));
        sendBroadcast(i);
    }

    public boolean isPackageInstalled() {
        final PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(SPOTIFY_PACKAGE);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
