package com.dicoding.picodiploma.finalsubmission.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderApp;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderMovie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TITLE_TAG = "settingsActivityTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                        setTitle(R.string.title_activity_settings);
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.settings));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }

    // Fragment header, menampilkan beranda pengaturan
    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey);
        }
    }

    // Fragment messages, menampilkan pengaturan bahasa
    public static class MessagesFragment extends PreferenceFragmentCompat {
        @BindView(R.id.card_view)
        CardView cardView;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.general_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.setting_localization, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ButterKnife.bind(this, view);

            cardView.setOnClickListener(v -> {
                Intent localeIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(localeIntent);
            });
        }
    }

    //
    public static class SyncFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.reminder_preferences, rootKey);

            // pengaturan on / off reminder app
            SharedPreferences sharedPrefApp = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isSharedPrefApp = sharedPrefApp.getBoolean("reminder_app", false);
            DailyReminderApp dailyReminderApp = new DailyReminderApp();

            if (isSharedPrefApp) {
                dailyReminderApp.setReminderApp(getContext());
            } else {
                dailyReminderApp.cancelReminderApp(getContext());
            }

            // pengaturan on / off reminder movie
            SharedPreferences sharedPrefMovie = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isSharedPrefMovie = sharedPrefMovie.getBoolean("reminder_movie", false);
            DailyReminderMovie reminderMovie = new DailyReminderMovie();

            if (isSharedPrefMovie) {
                reminderMovie.setReminderMovie(getContext());
            } else {
                reminderMovie.cancelReminderMovie(getContext());
            }
        }
    }
}
