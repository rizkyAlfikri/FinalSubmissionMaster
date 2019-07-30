package com.dicoding.picodiploma.finalsubmission;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.MovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.TvShowFragment;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderApp;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderMovie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoadCallback {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.navigation_movie:
                        fragment = new MovieFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
                                fragment, fragment.getClass().getSimpleName()).commit();
                        return true;
                    case R.id.navigation_tvshow:
                        fragment = new TvShowFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
                                fragment, fragment.getClass().getSimpleName()).commit();
                        return true;
                    case R.id.navigation_favorite:
                        fragment = new FavoriteFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
                                fragment, fragment.getClass().getSimpleName()).detach(fragment).attach(fragment).commit();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }

        SharedPreferences sharedPrefApp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSharedPrefApp = sharedPrefApp.getBoolean("reminder_app", false);
        DailyReminderApp dailyReminderApp = new DailyReminderApp();

        if (isSharedPrefApp) {
            dailyReminderApp.setReminderApp(this);
        } else {
            dailyReminderApp.cancelReminderApp(this);
        }

        SharedPreferences sharedPrefMovie = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSharedPrefMovie = sharedPrefMovie.getBoolean("reminder_movie", false);
        DailyReminderMovie reminderMovie = new DailyReminderMovie();

        if (isSharedPrefMovie) {
            reminderMovie.setReminderMovie(this);
        } else {
            reminderMovie.cancelReminderMovie(this);
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor cursor) {

    }
}
