package com.dicoding.picodiploma.finalsubmission;

import android.database.Cursor;
import android.os.Bundle;

import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.HomeFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.MovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity implements LoadCallback {

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
                                fragment, fragment.getClass().getSimpleName()).commit();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor cursor) {

    }
}
