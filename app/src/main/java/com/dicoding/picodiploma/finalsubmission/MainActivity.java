package com.dicoding.picodiploma.finalsubmission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.HomeFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.MovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.TvShowFragment;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderApp;
import com.dicoding.picodiploma.finalsubmission.reminder.DailyReminderMovie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoadCallback {
//    private MovieViewModel movieViewModel;
//    private MovieAdapter movieAdapter;
//    private List<MovieResults> listMovie = new ArrayList<>(), movieData;
//    @BindView(R.id.rv_movie)
//    RecyclerView rvMovie;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
                                fragment, fragment.getClass().getSimpleName()).commit();
                        return true;
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
            navView.setSelectedItemId(R.id.navigation_home);
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
//        rvMovie.setLayoutManager(new LinearLayoutManager(this));
//        rvMovie.setHasFixedSize(true);
//        movieAdapter = new MovieAdapter(this);
//        rvMovie.setAdapter(movieAdapter);
//        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        if (searchManager != null) {
//            SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            searchView.setQueryHint(getResources().getString(R.string.search_hint));
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    rvMovie.setVisibility(View.VISIBLE);
//                    ApiService apiService = RetrofitService.createService(ApiService.class);
//                    apiService.getQueryMovie(query).enqueue(new Callback<MovieResponse>() {
//                        @Override
//                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                            if (response.isSuccessful()) {
//                                if (response.body() != null) {
//                                    movieData = response.body().getResults();
//                                    for (MovieResults movieResults : movieData) {
//                                        listMovie.add(movieResults);
//                                    }
//                                    movieAdapter.setListMovie(listMovie);
//                                    movieAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<MovieResponse> call, Throwable t) {
//
//                        }
//                    });
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor cursor) {

    }
}
