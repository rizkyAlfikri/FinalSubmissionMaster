package com.dicoding.picodiploma.finalsubmission;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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

    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey);
        }
    }

    public static class MessagesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.general_preferences, rootKey);
        }
    }

    public static class SyncFragment extends PreferenceFragmentCompat {
        @BindView(R.id.switch_app)
        Switch switchApp;
        @BindView(R.id.switch_movie)
        Switch switchMovie;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.reminder_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.setting_notification, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ButterKnife.bind(this, view);
            if (getActivity() != null) {
                DailyReminderMovie reminderMovie = new DailyReminderMovie();
                SharedPreferences sharedPrefMovie = getActivity().getSharedPreferences("reminder_movie", MODE_PRIVATE);
                boolean isSharedPrefMovie = sharedPrefMovie.getBoolean("reminder_movie", false);
                switchMovie.setChecked(isSharedPrefMovie);
                switchMovie.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        reminderMovie.setReminderMovie(getContext());
                        SharedPreferences.Editor editor = sharedPrefMovie.edit();
                        editor.putBoolean("reminder_movie", isChecked);
                        editor.apply();
                    } else {
                        reminderMovie.cancelReminderMovie(getContext());
                        SharedPreferences.Editor editor = sharedPrefMovie.edit();
                        editor.putBoolean("reminder_movie", isChecked);
                        editor.apply();
                    }
                });

                DailyReminderApp dailyReminderApp = new DailyReminderApp();
                SharedPreferences sharedPrefApp = getActivity().getSharedPreferences("reminder_app", MODE_PRIVATE);
                boolean isSharedPrefApp = sharedPrefApp.getBoolean("reminder_app", false);
                switchApp.setChecked(isSharedPrefApp);
                switchApp.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        dailyReminderApp.setReminderApp(getContext());
                        SharedPreferences.Editor editor = sharedPrefApp.edit();
                        editor.putBoolean("reminder_app", isChecked);
                        editor.apply();
                    } else {
                        dailyReminderApp.cancelReminderApp(getContext());
                        SharedPreferences.Editor editor = sharedPrefApp.edit();
                        editor.putBoolean("reminder_app", isChecked);
                        editor.apply();
                    }
                });
            }
        }
    }
}
