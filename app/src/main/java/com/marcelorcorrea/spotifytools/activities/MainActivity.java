package com.marcelorcorrea.spotifytools.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelorcorrea.spotifytools.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.main:
                fragment = new MainFragment();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        fragmentTransaction.replace(R.id.container, fragment)
                .commit();
        return true;
    }
}